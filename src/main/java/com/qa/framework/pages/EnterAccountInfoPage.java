package com.qa.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;
public class EnterAccountInfoPage {

	private final Page page;
	private final Locator enterAccountInfoText;
	private final Locator mrRadioButton;
	private final Locator passwordTextbox;
	private final Locator daySelect;
	private final Locator monthSelect;
	private final Locator yearSelect;
	private final Locator countrySelect;
	private final Locator firstNameTextbox;
	private final Locator lastNameTextbox;
	private final Locator companyTextbox;
	private final Locator addressTextbox;
	private final Locator stateTextbox;
	private final Locator cityTextbox;
	private final Locator zipTextbox;
	private final Locator mobileNoTextbox;
	private final Locator createAccountTextbox;
	private final Locator userNameTextbox;
	private Locator continueButton;
	private  String  userName; 
	private  String  userpassword; 
	public EnterAccountInfoPage(Page page) {
		this.page = page;
		enterAccountInfoText = page.getByText("Enter Account Information", new Page.GetByTextOptions().setExact(true));
		mrRadioButton =page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Mr."));
		passwordTextbox =page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *"));
		daySelect = page.locator("#days");
		monthSelect= page.locator("#months");
		yearSelect = page.locator("#years");
		firstNameTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("First name *"));
		lastNameTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Last name *"));
		companyTextbox= page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Company").setExact(true));
		addressTextbox = page.getByRole(AriaRole.TEXTBOX ,new Page.GetByRoleOptions().setName("Address * (Street address, P."));
		countrySelect = page.locator("#country");
		stateTextbox= page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("State *"));
		cityTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("City *"));
		zipTextbox =page.locator("#zipcode"); 
		mobileNoTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Mobile Number *"));
		createAccountTextbox = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create Account"));
		userNameTextbox = page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Name *").setExact(true));
		}
	@Step("Enter Account Information")
	public void fillAccountInformation(String password, String day, String month, String year, String firstName, String lastName,
            String company, String address1, String country, String state,
            String city, String cipcode, String mobileNumber) {
		 
		userNameTextbox.waitFor();
		userName = userNameTextbox.inputValue();
		
		mrRadioButton.check();		
		passwordTextbox.fill(password);
		userpassword =password;
		daySelect.selectOption(day);
		monthSelect.selectOption(month);
		yearSelect.selectOption(year);
		
		firstNameTextbox.fill(firstName);
		lastNameTextbox.fill(lastName);
		companyTextbox.fill(company);
		
		addressTextbox.fill(address1);
		countrySelect.selectOption(country);
	
		stateTextbox.fill(state);
		cityTextbox.fill(city);
		zipTextbox.fill(cipcode);
		
		mobileNoTextbox.fill(mobileNumber);
		createAccountTextbox.click();
	}
	public Locator getUserCreatedLocator() {
		Locator txtLoggdInAsUser = page.getByText("Logged in as " + userName);
		return txtLoggdInAsUser;
	}
	public Locator getAccountCreatedLocator() {

		Locator txtAccountCreated = page.getByText("Account Created!");
		return txtAccountCreated;
	}
	@Step("Click on Continue button")
	public void clickContinueButton()
	{
		continueButton = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Continue").setExact(true));
		continueButton.click();
	}
	public Locator returnEnterAccountInfoHeading() {

		return enterAccountInfoText;

	}
	public String getNewUserDetails()
	{
		String userDetails = "User EmailID: "+userName + "@gmail.com";
		return  userDetails;
	}
}
