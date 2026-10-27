package com.qa.framework.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import io.qameta.allure.Step;

public class ProductPage {

	private final Page page;
	private final Locator womenLink;
	private final Locator dressLink;
	private final Locator dress1AddToCartLink;
	private final Locator dress2AddToCartLink;
	private final Locator continueToShoppingButton;
	private final Locator cartLink;
	private final  Locator viewCartLink;
	private final Locator topsLink;
	private final Locator sareeLink;

	public ProductPage(Page page) {
		this.page = page;
		this.womenLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Women")); 
        this.dressLink =   page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Dress "));
        this.topsLink =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Tops "));
        this.sareeLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Saree "));
		this.dress1AddToCartLink =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Add to cart")).first();
		this.dress2AddToCartLink =   page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Add to cart")).nth(2);
		this.continueToShoppingButton =  page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue Shopping"));
		this.cartLink =  page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName(" Cart"));
		this.viewCartLink = page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View Cart"));
		}
	
	@Step("Add {0} products")
	public void addToCartProducts(String productName)
	{
		switch(productName) {
			case "Women Dress":
				womenLink.click();
				dressLink.click();
				dress1AddToCartLink.click();
				clickContinueShoppingButton();
				dress2AddToCartLink.click();
				break;
			case "Women Tops":
				womenLink.click();
				topsLink.click();
				dress1AddToCartLink.click();
				break;
			case "Women Saree":
				womenLink.click();
				sareeLink.click();
				dress1AddToCartLink.click();
		}
	}
	public void clickContinueShoppingButton() {
		continueToShoppingButton.click();        
    }
	@Step("Click on Cart link")
    public void clickOnCartLink() {
    	cartLink.click();
    }
	@Step("Click on View Cart link")
    public void clickOnViewCartLink() {
    	viewCartLink.click();
    }
   
}
