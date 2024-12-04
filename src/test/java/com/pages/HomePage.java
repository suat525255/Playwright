package com.pages;

import com.microsoft.playwright.Page;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.utilities.Hooks.page;

@Listeners(com.utilities.Hooks.class)
public class HomePage {

    private String busCheckbox = "//label[@data-testid='home-hero-bus']/span";
    private String busOriginInput = "//input[@data-testid='bus-origin']";
    private String busOriginList = "#ui-id-9>li";
    private String busDestinationInput = "//input[@data-testid='bus-destination']";
    private String busDestinationList = "#ui-id-10>li";

    private String searchButton = "//button[@data-testid='bus-search']";
/*
    private  Page page;
    public HomePage(Page page) {
        this.page = page;
    }
*/
    void busSearchLocation(String originlocation, String destinationlocation) {
        page.click(busCheckbox);

        page.fill(busOriginInput, originlocation);
        page.locator(busOriginList).nth(0).click();
        page.fill(busDestinationInput, destinationlocation );
        page.locator(busDestinationList).nth(0).click();
    }

    @Test
    void  navigate() {

        busSearchLocation("Samsun", "İstanbul Anadolu");
        page.waitForTimeout(2000);
         page.click(searchButton);


    }








}
