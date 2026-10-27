package com.qa.tests.base;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Optional;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.qa.framework.factory.PlaywrightFactory;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;

public class BaseTest {
	protected PlaywrightFactory factory;
	protected Browser browser;
	protected Page page;

	@Parameters("browserName")
	@BeforeMethod
	@Step("Launch browser and navigate to 'Automation Exercise' site")
	public void setUp(@Optional("chromium")String browserName) {

		factory = new PlaywrightFactory();
		page = factory.initBrowser(browserName);
		factory.navigateToUrl();
		blockPoppingAds();
		checkAndAcceptCookies();
	}

	@Step("Blocking ads on website")
	public void blockPoppingAds() {
		page.context().route("**/*googlesyndication.com/**", route -> route.abort());
		page.context().route("**/*doubleclick.net/**", route -> route.abort());
		page.context().route("**/*googleadservices.com/**", route -> route.abort());
		page.context().route("**/pagead/**", route -> route.abort());
		// page.route("**/*google*", route -> route.abort());
		// page.route("**/*adservice*", route -> route.abort());
		// page.route("**/pagead/**", route -> route.abort());
	}

	@Step("Checking and accepting Cookies")
	public void checkAndAcceptCookies() {

		Locator consentButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Consent"));

		try {
			consentButton.click(new Locator.ClickOptions().setTimeout(3000));
		} catch (TimeoutError ignored) {
			System.out.println("No Cookies popup appreared");
		}
	}

	@AfterMethod(alwaysRun=true)
	public void tearDown(ITestResult result, Method method) {
		try {
			if (result.getStatus() == ITestResult.FAILURE) {
				captureScreenshot(PlaywrightFactory.getPage(), PlaywrightFactory.getBrowserName() + "-" + method.getName());
			}
		} catch (Exception e) {
			System.err.println("Error in TearDown: " + e.getMessage());
		} finally {
			PlaywrightFactory.quitBrowser(result, method);
		}
	}

	@Attachment(value = "Error-Screenshot", type = "image/png")
	public byte[] captureScreenshot(Page page, String testName) {
		if (page == null) {
			System.err.println("Screenshot failed: Page-Objekt is null.");
			return new byte[0];
		}

		byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));

		try {
			String screenshotPath = "target/screenshots/FAILED_" + testName + ".png";
			Files.createDirectories(Paths.get("target/screenshots/"));
			Files.write(Paths.get(screenshotPath), screenshotBytes);

			System.out.println("❌ Test failed. Screenshot saved to: " + screenshotPath);
		} catch (Exception e) {
			System.err.println("Error at saving the Screenshot-file: " + e.getMessage());
		}
		return screenshotBytes;
	}
}
