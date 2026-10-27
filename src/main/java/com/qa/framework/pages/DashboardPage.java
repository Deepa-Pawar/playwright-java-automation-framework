package com.qa.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class DashboardPage {
	private final Page page;
	private final Locator logoutButton;
	private final Locator deleteAccountButton;

	public DashboardPage(Page page) {
		this.page = page;
		this.logoutButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Logout"));
        this.deleteAccountButton =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Delete Account"));
	}
	public Locator getLoggedInAsLocator(String username) {
		Locator txtLoggdInAsUser = page.getByText("Logged in as " + username);
		return txtLoggdInAsUser;
	}
	@Step("Click on Logout button")
	public void clickOnLogoutButton() {
        logoutButton.click();
    }
	@Step("Click on Delete Account button")
    public void clickOnDeleteAccountButton() {
        deleteAccountButton.click();
    }
	
}
