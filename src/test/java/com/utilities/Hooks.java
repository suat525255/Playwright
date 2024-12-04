package com.utilities;

import com.microsoft.playwright.*;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.awt.*;
import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Hooks extends TestListenerAdapter {

    private Playwright playwright;
    private Browser browser;
    public static Page page;
    private BrowserContext context;



    @Override
    public void onTestStart(ITestResult result) {

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();

        BrowserFactory browserFactory = new BrowserFactory();
        String browserName = ConfigurationReader.get("browser");
        this.browser = browserFactory.getBrowser(browserName);
        this.context = browserFactory.createPageAndGetContext(this.browser, result);
        page = context.newPage();

        page.setViewportSize(width, height);
        page.navigate(ConfigurationReader.get("url"));

        page.click("#campaign-banner-modal-close");

    }


    @Override
    public void onTestSuccess(ITestResult result) {

        //if test success delete trace file
        cleanupOldTraces();

        try {
            if(context !=null){
               context.tracing().stop();
            }
        }finally {
                cleanup();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String tracePath = getTraceFilePath(result);
        //if test result fail save trace stop and save file
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get(tracePath)));

        cleanup();
    }

    public  String getTraceFilePath(ITestResult result) {
        String baseDir = "src/test/java/com/utilities/traceViewer/";
        String methodName = result.getMethod().getMethodName();
        String date = new SimpleDateFormat("_hh_mm_ss_ddMMyyyy").format(new Date());
        return baseDir + methodName + date + "-trace.zip";

    }

    private void cleanup(){
        //clean
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    private void cleanupOldTraces(){
        final long EXPIRATION_TIME = 86400000; // 24 HOUR
        File directory = new File("src/test/java/com/utilities/traceViewer/");
        File[] files = directory.listFiles();
        if (files != null) {
            long now = System.currentTimeMillis();
            for (File file : files) {
                if (now - file.lastModified() > EXPIRATION_TIME) {
                    if (!file.delete()) {
                        System.err.println("failed to delete old trace file: " + file.getPath());
                    }
                }
            }
        }
    }


}
