# Playwright Java Automation Framework

An end-to-end (E2E) UI test automation framework built with Playwright for Java, TestNG and Maven. It follows the Page Object Model (POM) pattern and runs critical user journeys against the demo site Automation Exercise in Chromium, Firefox and WebKit, in parallel.

## Key Features
1. Cross-browser testing : every test runs on Chromium, Firefox and WebKit, driven by testng.xml.
2. Parallel execution : the three browsers run concurrently (parallel="tests", thread-count="3"). Playwright, Browser, BrowserContext and Page objects are held in ThreadLocal, so threads never share state.
3. Page Object Model: locators and UI actions live in page classes, test logic lives in test classes.
4. Allure reporting: step-by-step reports using Allure.step and @Step, with severity, story and description metadata, plus a screenshot attached on failure.
5. Smart failure capture: on failure the framework saves a full-page screenshot, a Playwright trace (.zip) and a video (.webm). Videos of passing tests are deleted automatically to save disk space.
6. Externalized configuration and test data: the base URL and headless mode come from config.properties, test data from JSON files mapped to Java records via Jackson.
7. Ad and cookie handling: ad networks are blocked at network level and the cookie consent banner is accepted automatically.
8. CI ready: GitHub Actions workflow included.

## Tech Stack
Language	Java 25
Browser automation	Playwright for Java 1.62.0
Test runner	TestNG 7.12.0
Build tool	Maven
Reporting	Allure TestNG 2.35.4 (with AspectJ 1.9.25.1 for @Step)
Assertions	Playwright assertThat (UI) and AssertJ 3.26.3 (data)
Test data	Jackson 2.17.2 (JSON to Java records)
Logging	SLF4J Simple
CI	GitHub Actions

## Project Structure
.
├── .github/workflows/tests.yml        # CI pipeline
├── pom.xml                            # Dependencies, Surefire + AspectJ + Allure setup
├── testng.xml                         # Browser matrix and parallel configuration
└── src/
    ├── main/
    │   ├── java/com/qa/framework/
    │   │   ├── config/ConfigReader.java          # Loads config.properties
    │   │   ├── factory/PlaywrightFactory.java    # Browser/context/page lifecycle, tracing, video
    │   │   ├── pages/                            # Page objects
    │   │   │   ├── HomePage.java
    │   │   │   ├── LoginSignUpPage.java
    │   │   │   ├── EnterAccountInfoPage.java
    │   │   │   ├── DashboardPage.java
    │   │   │   ├── ContactUsPage.java
    │   │   │   ├── ProductPage.java
    │   │   │   ├── CartPage.java
    │   │   │   ├── CheckoutPage.java
    │   │   │   └── PaymentPage.java
    │   │   └── utils/TestDataLoader.java         # JSON -> record loader
    │   └── resources/config.properties
    └── test/
        ├── java/com/qa/tests/
        │   ├── base/BaseTest.java                # Setup/teardown, ad blocking, cookies, screenshots
        │   └── e2e/
        │       ├── EndToEndWorkflowTest.java     # Test scenarios
        │       ├── RegistrationData.java         # Data records
        │       └── UserLoginData.java
        └── resources/
            ├── testdata/                         # RegistrationData.json, UserLoginData.json
            └── test_upload.txt                   # File used by the Contact Us test

Generated at runtime (not committed): target/ (screenshots, traces, videos) and allure-results/.

## Automated Test Scenarios

All scenarios live in EndToEndWorkflowTest:

Test	Story	What it verifies
1. registerUser:	Register User	Creates a new user, verifies each registration step, then deletes the account to keep the site clean.
2. loginUser:	Login existing User	Logs in with existing credentials, checks the "Logged in as" label, logs out and verifies the Signup/Login link returns.
3. contactUs:	Contact Us	Submits the Contact Us form including a file upload and verifies the success message.
4. placeNewOrderLoginBeforeCheckout:	Place Order	Logs in, adds Dress, Top and Saree to the cart, validates the multi-line delivery address on checkout, enters payment details and verifies the order success message.
5. loginWithInvalidDetails:	Login with invalid login details	Verifies that wrong credentials produce the "incorrect email or password" error.

## Prerequisites
JDK 25 (the project compiles with maven.compiler.release=25)
Apache Maven 3.9+
An IDE such as IntelliJ IDEA or Eclipse (with the TestNG plugin if you want to run suites from the IDE)
Allure CLI (optional, only needed to view reports locally): see Reporting

## Installation
bash
git clone https://github.com/<your-username>/<your-repo>.git
cd <your-repo>
mvn clean install -DskipTests
Install Playwright browsers

Playwright for Java downloads the browser binaries automatically the first time a Playwright instance is created. To install them explicitly up front (recommended on CI and on fresh Linux machines, because --with-deps also installs the required OS libraries):
-------------
bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps"
Configuration

Settings are stored in src/main/resources/config.properties:

properties
url=https://www.automationexercise.com/
headless=false
Property	Description	Default
url	Base URL of the application under test	https://www.automationexercise.com/
headless	true runs browsers without a visible window, false opens them	false

The browser is not read from this file. It is set per <test> block in testng.xml through the browserName parameter. Supported values: chromium, firefox, webkit. Any other value throws IllegalArgumentException. If a test class is run without testng.xml (for example directly from the IDE), BaseTest falls back to chromium.

Test Data

Test data is kept outside the test code as JSON files on the classpath (testdata/) and mapped to Java records by TestDataLoader:

File	Record	Used for
RegistrationData.json	RegistrationData (+ PaymentDetails)	New user registration, address verification and payment details
UserLoginData.json	UserLoginData	Valid and invalid login credentials, Contact Us form

Example UserLoginData.json:

Important: loginUser, contactUs and placeNewOrderLoginBeforeCheckout need an account that already exists on the site. The address data of that account must match RegistrationData.json, because the checkout test compares the delivery address against that file.

Only use throwaway demo accounts and the test card from the demo site. Never commit real credentials or personal data.

## Running the Tests
1. From the IDE:Right-click testng.xml and choose Run As → TestNG Suite. This runs the full browser matrix.

2. From the command line
bash
# Run the whole suite (Chromium + Firefox + WebKit, in parallel)
mvn test

# Run headed or headless: change `headless` in config.properties

Surefire is configured to use testng.xml, so mvn test always runs the full browser matrix.

Running a single browser

Comment out or remove the other <test> blocks in testng.xml. Example, Chromium only:

xml
<suite name="Suite" parallel="tests" thread-count="3">
    <test name="Chrome Tests">
        <parameter name="browserName" value="chromium" />
        <classes>
            <class name="com.qa.tests.e2e.EndToEndWorkflowTest" />
        </classes>
    </test>
</suite>
Reporting and Failure Artifacts
Allure report

Raw results are written to allure-results/ after each run. Install the Allure CLI (brew install allure, scoop install allure, or npm install -g allure), then:

bash
# Open the interactive report in the browser
allure serve allure-results

# Or generate a static HTML report
allure generate allure-results --clean -o allure-report

The report shows step-by-step execution, severity and story grouping, stack traces for failures and the failure screenshot.

Failure artifacts

On a failed test the framework stores the following files under target/. The file names are prefixed with the browser name, for example chromium-loginUser:

## Artifact	Location	Notes
Screenshot	target/screenshots/FAILED_<browser>-<test>.png	Full page, also attached to the Allure report
Trace	target/traces/<browser>-<test>-trace.zip	Open with mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace <file>"
Video	target/videos/<browser>-<test>.webm	Recorded for every test, kept only if the test fails
CI/CD

The workflow .github/workflows/tests.yml runs on every push and pull request to main or master:

Checks out the code and sets up Temurin JDK 25 with Maven caching
Installs Playwright browsers with system dependencies
Runs mvn test
Uploads allure-results/ as an artifact (always)
Uploads screenshots, traces and videos as the failure-artifacts artifact (only on failure)

To view a CI report, download the allure-results artifact from the workflow run and run allure serve on it locally.

Design Notes
1. Test isolation: each test method gets its own Playwright instance, browser, context and page (@BeforeMethod), and everything is closed in @AfterMethod(alwaysRun = true), so tests do not affect each other.
2. Thread safety: PlaywrightFactory stores all Playwright objects in ThreadLocal, which is what makes parallel execution across browsers safe.
3. Resilient teardown: the screenshot, trace, video and browser shutdown steps are each guarded, so a failure in one does not prevent the rest of the cleanup.
4. Assertions: Playwright's assertThat(locator) is used for UI state because it auto-waits and retries, while AssertJ is used for plain data such as the address lines.

**Known Limitations and Roadmap**
1. headless can currently only be changed in config.properties (not via -Dheadless=true)
2. Add a Maven property for the suite file so single-browser or smoke suites can be run with -DsuiteXmlFile=...
3. Attach trace and video files to the Allure report (currently they are saved to target/ only)
4. Add TestNG groups (smoke, regression) and a retry analyzer for flaky tests
5. Add API-based setup/cleanup (user creation and deletion) and reuse login via Playwright storageState
6. Publish the Allure report to GitHub Pages from CI
7. Docker support
Author

Deepa Pawar LinkedIn | GitHub
