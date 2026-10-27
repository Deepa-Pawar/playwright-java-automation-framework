package com.qa.tests.e2e;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.qa.framework.pages.CartPage;
import com.qa.framework.pages.CheckoutPage;
import com.qa.framework.pages.ContactUsPage;
import com.qa.framework.pages.DashboardPage;
import com.qa.framework.pages.EnterAccountInfoPage;
import com.qa.framework.pages.HomePage;
import com.qa.framework.pages.LoginSignUpPage;
import com.qa.framework.pages.PaymentPage;
import com.qa.framework.pages.ProductPage;
import com.qa.framework.utils.TestDataLoader;
import com.qa.tests.base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

public class EndToEndWorkflowTest extends BaseTest {

	private RegistrationData userData;
	private UserLoginData userLoginData;

	@BeforeClass(alwaysRun = true)
	public void loadTestData() {
		this.userData = TestDataLoader.load("RegistrationData.json", RegistrationData.class);
		this.userLoginData = TestDataLoader.load("UserLoginData.json", UserLoginData.class);
	}

	@Description("I am able to create New User successfully ")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Register User")
	@Test(description = "Verify that new user is getting create successfully")
	public void registerUser() {
		
		HomePage homePage = new HomePage(page);
		Allure.step("Verify page title as: Automation Exercise", () -> {
			assertThat(page).hasTitle("Automation Exercise");
		});
		homePage.clickOnSignUpLoginOption();

		LoginSignUpPage loginSignupPage = new LoginSignUpPage(page);
		Allure.step("Check that New User SignUp! text displays", () -> {
			assertThat(loginSignupPage.returnNewUserSignUpText()).isVisible();
		});
		String[] userDetails = loginSignupPage.enterUsernameAndEmailID();
		Allure.step("New user Details: Username: "+userDetails[0]+ " EmailID: "+userDetails[1]);
		
		EnterAccountInfoPage accInfoPage = new EnterAccountInfoPage(page);
		Allure.step("Check that Enter Account Information page displays", () -> {
			assertThat(accInfoPage.returnEnterAccountInfoHeading()).isVisible();
		});

		accInfoPage.fillAccountInformation(userData.password(), userData.day(), userData.month(), userData.year(),
				userData.firstName(), userData.lastName(), userData.company(), userData.address(), userData.country(),
				userData.state(), userData.city(), userData.zipcode(), userData.mobileNumber());

		Allure.step("Check that Account Created text is visible", () -> {
			assertThat(accInfoPage.getAccountCreatedLocator()).isVisible();
		});

		accInfoPage.clickContinueButton();
		Allure.step("New User created and logged in as " + accInfoPage.getNewUserDetails(), () -> {
			assertThat(accInfoPage.getUserCreatedLocator()).isVisible();
		});
		
		DashboardPage dashboardPage = new DashboardPage(page);
		dashboardPage.clickOnDeleteAccountButton();
		Allure.step("Verify success message: User deleted is visible. ", () -> {
			assertThat(homePage.getAccountDeletedLocator()).isVisible();
		});
	}

	@Description("I am able to login User successfully ")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Login existing User")
	@Test(description = "Verify that existing user is able to login & log out successfully")
	public void loginUser() {

		HomePage homePage = new HomePage(page);
		Allure.step("Verify page title as: Automation Exercise", () -> {
			assertThat(page).hasTitle("Automation Exercise");
		});
		homePage.clickOnSignUpLoginOption();

	    Allure.step("Login existing user into application");
		LoginSignUpPage loginSignupPage = new LoginSignUpPage(page);
		loginSignupPage.enterLoginDetails(userLoginData.emailID(), userLoginData.password());
		loginSignupPage.clickLoginButton();

		DashboardPage dashboardPage = new DashboardPage(page);
		Allure.step("Verify logged in as: " + userLoginData.username(), () -> {
			assertThat(dashboardPage.getLoggedInAsLocator(userLoginData.username())).isVisible();
		});

		dashboardPage.clickOnLogoutButton();
		Allure.step("Verify that user has logged out and Signup/login page displays ", () -> {
			assertThat(homePage.getSignupLoginLocator()).isVisible();
		});
	}

	@Description("User able to Contact using Contact Us functionality")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Contact Us")
	@Test(description = "Verify that existing user is able to Contact successfully")
	public void contactUs() {

		String fileToUpload = "src/test/resources/test_upload.txt";

		HomePage homePage = new HomePage(page);
		Allure.step("Verify page title as: Automation Exercise", () -> {
			assertThat(page).hasTitle("Automation Exercise");
		});
		Allure.step("Click on Contact Us Link");
		homePage.clickOnContactUsOption();

		Allure.step("Enter user details and verify file upload");
		ContactUsPage contactUsPage = new ContactUsPage(page);
		contactUsPage.fillContactUsForm(userLoginData.username(), userLoginData.emailID(), fileToUpload);
		contactUsPage.clickOnSubmitButton();
		Allure.step("Verify Success message:Success! Your details have been submitted successfully.", () -> {
			assertThat(contactUsPage.getSuccessMessageLocator()).isVisible();
		});
	}

	@Description("I am able to place new order successfully ")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Place Order")
	@Test(description = "Verify that user is able to place Order: Register before Checkout")
	public void placeNewOrderLoginBeforeCheckout() {

		HomePage homePage = new HomePage(page);
		Allure.step("Verify page title as : Automation Exercise", () -> {
			assertThat(page).hasTitle("Automation Exercise");
		});
		homePage.clickOnSignUpLoginOption();

		Allure.step("Login existing user into application");
		LoginSignUpPage loginSignupPage = new LoginSignUpPage(page);
		loginSignupPage.enterLoginDetails(userLoginData.emailID(), userLoginData.password());
		loginSignupPage.clickLoginButton();

		DashboardPage dashboardPage = new DashboardPage(page);
		Allure.step("Verify logged in as: " + userLoginData.username(), () -> {
			assertThat(dashboardPage.getLoggedInAsLocator(userLoginData.username())).isVisible();
		});

		Allure.step("Add Products (Dress, Top, Saree) to Cart");
		homePage.clickOnProductsOption();
		ProductPage productPage = new ProductPage(page);
		productPage.addToCartProducts("Women Dress");
		productPage.clickContinueShoppingButton();
		productPage.addToCartProducts("Women Tops");
		productPage.clickContinueShoppingButton();
		productPage.addToCartProducts("Women Saree");
		productPage.clickOnViewCartLink();

		CartPage cartPage = new CartPage(page);
		cartPage.clickOnProceedToCheckoutText();

		CheckoutPage checkoutPage = new CheckoutPage(page);
		List<String> actualAddressLines = checkoutPage.getDeliveryAddressDetails();

		String expectedFullName = userData.firstName() + " " + userData.lastName();
		Allure.step("Verify Delivery Address details on Checkout Page", () -> {

			Allure.addAttachment("Input Test Data", "text/plain", "Validating details for user: " + expectedFullName);
			assertThat(actualAddressLines).as("Verifying User Full Name")
					.anyMatch(line -> line.contains(expectedFullName));
			assertThat(actualAddressLines).as("Check Delivery Street Address")
					.anyMatch(line -> line.contains(userData.address()));
			assertThat(actualAddressLines).as("Check Delivery City").anyMatch(line -> line.contains(userData.city()));
			assertThat(actualAddressLines).as("Check Delivery State").anyMatch(line -> line.contains(userData.state()));
			assertThat(actualAddressLines).as("Check Delivery Zipcode")
					.anyMatch(line -> line.contains(userData.zipcode()));
			assertThat(actualAddressLines).as("Check Country").anyMatch(line -> line.contains(userData.country()));
			assertThat(actualAddressLines).as("Check Mobile Numner")
					.anyMatch(line -> line.contains(userData.mobileNumber()));
			
		});
		Allure.step("Success Comment: All address line verified successfully.");
		checkoutPage.clickOnPlaceOrderButton();
		
		Allure.step("Enter payment details and make payment");
		PaymentPage paymentPage = new PaymentPage(page);
		paymentPage.enterPaymentDetails(userData.paymentDetails().nameOnCard(),userData.paymentDetails().cardNumber(),userData.paymentDetails().cvc(),userData.paymentDetails().expirationMonth(),userData.paymentDetails().expirationYear());
		paymentPage.clickOnPayAndConfirmOrderButton();
		Allure.step("Verify order placed successfully message displays", () -> {
			assertThat(paymentPage.getOrderSuccessMessageLocator()).isVisible();
		});
	}
	@Description("User is not able to log in with an incorrect email address or password")
	@Severity(SeverityLevel.CRITICAL)
	@Story("Login with invalid login details")
	@Test(description = "Verify that log in with an incorrect email address or password")
	public void loginWithInvalidDetails() {

		HomePage homePage = new HomePage(page);
		Allure.step("Verify page title as: Automation Exercise", () -> {
			assertThat(page).hasTitle("Automation Exercise");
		});
		homePage.clickOnSignUpLoginOption();
		
		Allure.step("Login as invalid user into application");
		LoginSignUpPage loginSignupPage = new LoginSignUpPage(page);
		loginSignupPage.enterLoginDetails(userLoginData.invalid_emailID(), userLoginData.invalid_password());
		loginSignupPage.clickLoginButton();
		
		Allure.step("Verify that incorrect email or password message displays", () -> {
			assertThat(loginSignupPage.getIncorrectUserDetailsLocator()).isVisible();
		});
	}
}
