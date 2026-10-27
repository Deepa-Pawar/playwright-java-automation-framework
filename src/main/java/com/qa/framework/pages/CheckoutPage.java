package com.qa.framework.pages;

import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CheckoutPage {
	
	private final Page page;
	private final Locator deliveryAddressBlock;
	private final Locator PlaceOrderButton;

	public CheckoutPage(Page page) {
		this.page = page;
		this.deliveryAddressBlock = page.locator("#address_delivery");
		this.PlaceOrderButton = page.getByText("Place Order");
	}
	public List<String> getDeliveryAddressDetails() {
		deliveryAddressBlock.waitFor();
		return deliveryAddressBlock.locator("li").allTextContents();
	}
	public void clickOnPlaceOrderButton() {
		PlaceOrderButton.click();
	}
}
