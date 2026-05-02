package utils.factory;

import com.microsoft.playwright.*;
import java.util.*;

public class PlaywrightDriverFactory {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext context;
    private static Page page;

    private static final Map<String, BrowserType.LaunchOptions> BROWSER_OPTIONS = new HashMap<>();

    static {
        BrowserType.LaunchOptions headless = new BrowserType.LaunchOptions().setHeadless(true);
        BrowserType.LaunchOptions headed = new BrowserType.LaunchOptions().setHeadless(false);
        BROWSER_OPTIONS.put("headless", headless);
        BROWSER_OPTIONS.put("headed", headed);
    }

    public static void init(String browserType, boolean headless) {
        if (playwright == null) {
            playwright = Playwright.create();
        }

        BrowserType.LaunchOptions options = headless
                ? BROWSER_OPTIONS.get("headless")
                : BROWSER_OPTIONS.get("headed");

        switch (browserType.toLowerCase()) {
            case "chromium":
                browser = playwright.chromium().launch(options);
                break;
            case "firefox":
                browser = playwright.firefox().launch(options);
                break;
            case "webkit":
                browser = playwright.webkit().launch(options);
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }

        context = browser.newContext();
        page = context.newPage();
    }

    public static Page getPage() {
        if (page == null) throw new IllegalStateException("Playwright not initialized. Call init() first.");
        return page;
    }

    public static BrowserContext getContext() {
        return context;
    }

    /**
     * Closes the current context and page, then opens a fresh context and page on the
     * same browser instance. Simulates a new browser session without relaunching the browser.
     */
    public static void resetSession() {
        if (page != null) { page.close(); page = null; }
        if (context != null) { context.close(); context = null; }
        if (browser == null) throw new IllegalStateException("Browser not initialized. Call init() first.");
        context = browser.newContext();
        page = context.newPage();
    }

    public static void close() {
        if (page != null) page.close();
        if (context != null) context.close();
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
        playwright = null;
        browser = null;
        context = null;
        page = null;
    }
}
