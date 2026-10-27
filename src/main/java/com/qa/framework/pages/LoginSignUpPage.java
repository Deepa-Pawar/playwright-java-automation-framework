package com.qa.framework.pages;

import java.util.Random;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class LoginSignUpPage {

	private final Page page;
	private final Locator userSignUpHeading;
	private final Locator nameTextbox;
	private final Locator emailAdressSignUpTextbox;
	private final Locator signupButton;
	private final Locator emailAdressForLoginTextbox;
	private final Locator passwordForLoginTextbox;
	private final Locator loginButton;
	private final Locator incorrectUserDetailsMessageText;

	public LoginSignUpPage(Page page) {
		this.page = page;
		userSignUpHeading = page.getByText("New User Signup!", new Page.GetByTextOptions().setExact(true));
		nameTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name"));
		emailAdressSignUpTextbox = page.locator("form").filter(new Locator.FilterOptions().setHasText("Signup"))
				.getByPlaceholder("Email Address");
		signupButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Signup"));
		emailAdressForLoginTextbox = page.locator("form").filter(new Locator.FilterOptions().setHasText("Login"))
				.getByPlaceholder("Email Address");
		passwordForLoginTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password"));
		loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
		incorrectUserDetailsMessageText = page.getByText("Your email or password is incorrect!");
	}

	@Step("Entering randomly generated Username and Email ID for signup")
	public String[] enterUsernameAndEmailID() {

		if (userSignUpHeading.isVisible()) {
			Random rand = new Random();
			int randomInt = rand.nextInt(5000);

			String username = "testUser" + randomInt;
			String email = "testUser" + randomInt + "@gmail.com";

			nameTextbox.fill(username);
			emailAdressSignUpTextbox.fill(email);
			signupButton.click();

			return new String[] { username, email };
		} else {
			return new String[] { "", "" };
		}
	}
	public Locator returnNewUserSignUpText() {

		return userSignUpHeading;

	}
	@Step("User Details:")
	public void enterLoginDetails(String emailID, String password) {
		emailAdressForLoginTextbox.fill(emailID);
		passwordForLoginTextbox.fill(password);
	}
	@Step("Click on Login button")
	public void clickLoginButton() {
		loginButton.click();
	}
	public Locator getIncorrectUserDetailsLocator() {
		return incorrectUserDetailsMessageText;
	}
}
