package com.qa.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

import io.qameta.allure.Step;

public class CartPage {
	
	private final Page page;
	private final Locator proccedToCheckOutText;
	
	public CartPage(Page page) {
		this.page = page;
		this.proccedToCheckOutText = page.getByText("Proceed To Checkout");
	}
	@Step("Click on Proceed To Checkout button")
	public void clickOnProceedToCheckoutText() {
		proccedToCheckOutText.click();
	}
}
