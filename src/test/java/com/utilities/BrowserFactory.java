package com.utilities;

import com.microsoft.playwright.*;
import org.testng.ITestResult;

public class BrowserFactory {

    private Playwright playwright;

    public BrowserFactory(){
        playwright = Playwright.create();
    }


    public Browser getBrowser(String browserName){
        boolean headless = Boolean.parseBoolean(ConfigurationReader.get("headless"));
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions().setHeadless(headless);
        BrowserType browserType;

        switch (browserName.toLowerCase()){
            case "chrome":
                browserType = playwright.chromium();
                launchOptions.setChannel("chrome");
                break;
            case "chromium":
                browserType = playwright.chromium();
                break;
            case "firefox":
                browserType = playwright.firefox();
                break;
            case "safari":
                browserType = playwright.webkit();
                break;
            case "edge":
                browserType = playwright.firefox();
                launchOptions.setChannel("msedge");
                break;
            default:
                String message = "Unsupported browser: " + browserName;
                message += "Supported browsers: chromium, firefox, webkit";
                throw new IllegalArgumentException("Unsupported browser: " + message);
        }
        return browserType.launch(launchOptions);
    }

    public BrowserContext createPageAndGetContext(Browser browser, ITestResult result){

        BrowserContext context = browser.newContext();
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true)
                .setName(result.getMethod().getMethodName()));// Method name: trace name

        return context;
    }


}
