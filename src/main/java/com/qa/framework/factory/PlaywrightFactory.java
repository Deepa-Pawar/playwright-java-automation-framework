package com.qa.framework.factory;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import org.testng.ITestResult;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Tracing;
import com.qa.framework.config.*;

public class PlaywrightFactory {

	private Properties prop;

	private static final ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
	private static final ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
	private static final ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();
	private static final ThreadLocal<Page> tlPage = new ThreadLocal<>();

	public static Page getPage() {
		return tlPage.get();
	}
	public static String getBrowserName() {
	    Browser b = tlBrowser.get();
	    return (b != null) ? b.browserType().name() : "unknown";
	}
	public Page initBrowser(String browserName) {

		ConfigReader configReader = new ConfigReader();
		prop = configReader.initProperties();

		boolean headless = Boolean.parseBoolean(prop.getProperty("headless").trim());

		tlPlaywright.set(Playwright.create());

		BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(headless).setArgs(Arrays.asList("--start-maximized"));

		switch (browserName.toLowerCase()) {
		case "chromium":
			tlBrowser.set(tlPlaywright.get().chromium().launch(options));
			break;
		case "firefox":
			tlBrowser.set(tlPlaywright.get().firefox().launch(options));
			break;
		case "webkit":
			tlBrowser.set(tlPlaywright.get().webkit().launch(options));
			break;
		default:
			throw new IllegalArgumentException("Browser not supported: " + browserName);
		}
		BrowserContext contextInstance = tlBrowser.get().newContext(
	            new Browser.NewContextOptions()
	                    .setRecordVideoDir(Paths.get("target/videos/"))
	                    .setRecordVideoSize(1280, 720)                 
	                    .setViewportSize(null)                         
	    );
		
		tlContext.set(contextInstance);
		tlContext.get().tracing()
				.start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
		Page pageInstance = tlContext.get().newPage();
		tlPage.set(pageInstance);
		
		return tlPage.get();
	}

	public void navigateToUrl() {

		String url = prop.getProperty("url").trim();
		tlPage.get().navigate(url);

	}

	public static void quitBrowser(ITestResult result, Method method) {
	    // Read everything you need BEFORE anything is closed
	    String testName = getBrowserName() + "-" + method.getName();
	    boolean failed = result.getStatus() == ITestResult.FAILURE;
	    BrowserContext context = tlContext.get();
	    Page page = tlPage.get();

	    try {
	        // Only this part depends on the context existing
	        if (context != null) {

	            // 1. Trace
	            try {
	                if (failed) {
	                    String tracePath = "target/traces/" + testName + "-trace.zip";
	                    context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracePath)));
	                    System.out.println("Trace saved for failed test: " + tracePath);
	                } else {
	                    context.tracing().stop(new Tracing.StopOptions().setPath(null));
	                }
	            } catch (Exception e) {
	                System.err.println("Failed to save Playwright trace: " + e.getMessage());
	            }

	            // 2. Close page and context, then handle the video
	            try {
	                if (page != null) {
	                    page.close();
	                }
	                context.close();

	                if (page != null && page.video() != null) {
	                    if (failed) {
	                        Files.createDirectories(Paths.get("target/videos/"));
	                        page.video().saveAs(Paths.get("target/videos/" + testName + ".webm"));
	                        System.out.println("Video saved for failed test: target/videos/" + testName + ".webm");
	                    } else {
	                        deleteVideoWithRetry(page, 3, 300);
	                    }
	                }
	            } catch (Exception e) {
	                System.err.println("Failed to finalize video or close context: " + e.getMessage());
	            }
	        }
	    } finally {
	        // ALWAYS runs, even if context is null or something above threw an exception
	        try {
	            if (tlBrowser.get() != null) {
	                tlBrowser.get().close();
	            }
	        } catch (Exception e) {
	            System.err.println("Failed to close browser: " + e.getMessage());
	        }
	        try {
	            if (tlPlaywright.get() != null) {
	                tlPlaywright.get().close();
	            }
	        } catch (Exception e) {
	            System.err.println("Failed to close Playwright: " + e.getMessage());
	        }

	        tlPage.remove();
	        tlContext.remove();
	        tlBrowser.remove();
	        tlPlaywright.remove();
	    }
	}

	private static void deleteVideoWithRetry(Page page, int attempts, long delayMs) {
		for (int i = 1; i <= attempts; i++) {
			try {
				page.video().delete();
				return; // success
			} catch (Exception e) {
				if (i == attempts) {
					System.err.println("Could not delete video after " + attempts + " attempts: " + e.getMessage());
				} else {
					try {
						Thread.sleep(delayMs);
					} catch (InterruptedException ignored) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}
	}
}
