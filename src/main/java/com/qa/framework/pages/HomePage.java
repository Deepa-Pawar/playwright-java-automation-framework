package com.qa.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class HomePage {
	private final Page page;
	private final Locator signUpLoginLink;
	private final Locator contactUsLink;
	private final Locator productsLink;
	private final Locator accountDeletedText;

	public HomePage(Page page) {
		this.page = page;
		signUpLoginLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Signup / Login"));
		contactUsLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Contact us"));
		this.productsLink =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Products"));
		this.accountDeletedText =page.getByText("Account Deleted!");
	}
	
	@Step("Click on Sign up/Login Option on Home Page")
	public void clickOnSignUpLoginOption() {

		signUpLoginLink.click();
	}
	public void clickOnContactUsOption() {
		contactUsLink.click();
	}
	public Locator getSignupLoginLocator()
	{
		return signUpLoginLink;
	}
	@Step("Click on Products Option")
	public void clickOnProductsOption() {
		productsLink.click();
	}
	public Locator getAccountDeletedLocator()
	{
		return accountDeletedText;
	}
}
