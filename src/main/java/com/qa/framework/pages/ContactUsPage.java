package com.qa.framework.pages;

import java.nio.file.Paths;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class ContactUsPage {
	
	private final Page page;

	private final Locator nameInput;
	private final Locator emailInput;
	private final Locator fileUploadInput;
	private final Locator submitButton;
	private final Locator successText;

	public ContactUsPage(Page page) {
		this.page = page;
		this.nameInput =  page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name"));
		this.emailInput = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email").setExact(true));
		this.fileUploadInput =  page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Choose File"));
		this.submitButton = page.locator("//input[@data-qa='submit-button']");
		this.successText  = page.locator("#contact-page").getByText("Success! Your details have been submitted successfully.");
	}
	@Step("User details :")
	public void fillContactUsForm(String name, String email, String relativeFilePath) {
		
		nameInput.fill(name);
		emailInput.fill(email);
		fileUploadInput.setInputFiles(Paths.get(relativeFilePath));
	}
	@Step("Click on Submit button")
	public void clickOnSubmitButton() {
		page.onceDialog(dialog -> {
	        System.out.println(String.format("%s", dialog.message()));
	        dialog.accept();
	      });
		submitButton.click();
	}
	public Locator getSuccessMessageLocator() {
		return successText;
	}
}
