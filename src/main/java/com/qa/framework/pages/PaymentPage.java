package com.qa.framework.pages;

import java.util.List;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class PaymentPage {

	private final Page page;
	private final Locator nameOnCardInput;
	private final Locator cardNumberInput;
	private final Locator cvcInput;
	private final Locator expirationMonthInput;
	private final Locator expirationYearInput;
	private final Locator payAndConfirmOrderButton;
	private Locator orderSuccessText;

	public PaymentPage(Page page) {
		this.page = page;
		this.nameOnCardInput = page.locator("//input[@name='name_on_card']");
		this.cardNumberInput = page.locator("//input[@name='card_number']");
		this.cvcInput =page.locator("//input[@name='cvc']");
		this.expirationMonthInput = page.locator("//input[@name='expiry_month']");
		this.expirationYearInput = page.locator("//input[@name='expiry_year']");
		this.payAndConfirmOrderButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pay and Confirm Order"));
		this.orderSuccessText = page.getByText("Congratulations! Your order has been confirmed!");
		
	}
	@Step("Payment details:")
	public void enterPaymentDetails(String name,String cardNumber,String CVC,String expMonth,String expYear) {
		nameOnCardInput.fill(name);
		cardNumberInput.fill(cardNumber);
		cvcInput.fill(CVC);
		expirationMonthInput.fill(expMonth);
		expirationYearInput.fill(expYear);
	}
	@Step("Click on Pay and Confirm button")
	public void clickOnPayAndConfirmOrderButton()
	{
		payAndConfirmOrderButton.click();
	}
	public Locator getOrderSuccessMessageLocator()
	{
		return orderSuccessText;
		
	}
}
