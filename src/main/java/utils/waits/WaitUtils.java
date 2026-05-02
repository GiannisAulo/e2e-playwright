package utils.waits;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.concurrent.TimeUnit;

public class WaitUtils {
    private static final long DEFAULT_TIMEOUT = 10000; // 10 seconds in milliseconds
    private static final long DEFAULT_POLL_INTERVAL = 250; // 250 milliseconds

    private Page page;

    public WaitUtils(Page page) {
        this.page = page;
    }

    // ==================== Page Load Waits ====================

    /**
     * Wait for page to be fully loaded (document.readyState = complete + network idle + loading spinners)
     */
    public void waitForLoad() {
        try {
            page.waitForFunction("document.readyState === 'complete'");
            page.waitForLoadState(LoadState.NETWORKIDLE);
            waitForLoadingImage();
            sleep(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutError e) {
            throw new RuntimeException("Timeout waiting for page to load completely");
        }
    }

    /**
     * Wait for page to be fully loaded with custom timeout
     */
    public void waitForLoad(int timeout) {
        try {
            page.waitForLoadState(LoadState.NETWORKIDLE, new Page.WaitForLoadStateOptions().setTimeout(timeout));
            waitForLoadingImage();
        } catch (TimeoutError e) {
            throw new RuntimeException("Page did not reach network idle state after " + timeout + " ms");
        }
    }

    /**
     * Wait for DOM content to be loaded
     */
    public void waitForDOMContentLoaded() {
        try {
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        } catch (TimeoutError e) {
            throw new RuntimeException("DOM content not loaded");
        }
    }

    /**
     * Wait for loading spinners/images to disappear
     */
    public void waitForLoadingImage() {
        // Wait for spinner box to be hidden
        waitForHidden(".spinner-box", (int) DEFAULT_TIMEOUT);

        // Wait for active spinner container to be hidden
        waitForHidden("div.spinner-container.active", (int) DEFAULT_TIMEOUT);
    }

    // ==================== Element Visibility Waits ====================

    /**
     * Wait for element to be visible
     */
    public void waitForVisible(String selector, int timeout) {
        Locator locator = page.locator(selector);
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.VISIBLE));
        } catch (TimeoutError e) {
            throw new RuntimeException("Element not visible after " + timeout + " ms: " + selector);
        }
    }

    /**
     * Wait for element to be visible with default timeout
     */
    public void waitForVisible(String selector) {
        waitForVisible(selector, (int) DEFAULT_TIMEOUT);
    }

    /**
     * Wait for locator to be visible
     */
    public void waitForElement(Locator locator) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(DEFAULT_TIMEOUT)
                    .setState(WaitForSelectorState.VISIBLE));
        } catch (TimeoutError e) {
            throw new RuntimeException("Element is NOT present/visible");
        }
    }

    /**
     * Wait for element to be present in DOM (attached)
     */
    public void waitForElementToBePresent(String selector, int timeout) {
        Locator locator = page.locator(selector);
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.ATTACHED));
        } catch (TimeoutError e) {
            throw new RuntimeException("Element not present after " + timeout + " ms: " + selector);
        }
    }

    /**
     * Wait for element to be present with default timeout
     */
    public void waitForElementToBePresent(String selector) {
        waitForElementToBePresent(selector, (int) DEFAULT_TIMEOUT);
    }

    /**
     * Wait for element to be hidden/invisible
     */
    public void waitForHidden(String selector, int timeout) {
        try {
            // First check if element exists
            if (page.locator(selector).count() == 0) {
                return; // Element doesn't exist, so it's "hidden"
            }

            Locator locator = page.locator(selector);
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.HIDDEN));
        } catch (TimeoutError e) {
            // Element might not exist at all, which is acceptable for "hidden"
            if (page.locator(selector).count() == 0) {
                return;
            }
            throw new RuntimeException("Element not hidden after " + timeout + " ms: " + selector);
        }
    }

    /**
     * Wait for element to be hidden with default timeout
     */
    public void waitForHidden(String selector) {
        waitForHidden(selector, (int) DEFAULT_TIMEOUT);
    }

    /**
     * Wait for invisibility of element
     */
    public void waitForInvisibilityOfElement(String selector) {
        waitForHidden(selector, (int) DEFAULT_TIMEOUT);
    }

    // ==================== Element State Waits ====================

    /**
     * Wait for element to be clickable (visible + enabled)
     */
    public void waitForElementToBeClickable(String selector, int timeout) {
        Locator locator = page.locator(selector);
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.VISIBLE));
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.ATTACHED));
        } catch (TimeoutError e) {
            throw new RuntimeException("Element not clickable after " + timeout + " ms: " + selector);
        }
    }

    /**
     * Wait for element to be clickable with default timeout
     */
    public void waitForElementToBeClickable(String selector) {
        waitForElementToBeClickable(selector, (int) DEFAULT_TIMEOUT);
    }

    /**
     * Wait for dropdown element to be clickable
     */
    public void waitForDropdownElement(String selector) {
        waitForElementToBeClickable(selector);
    }

    /**
     * Wait for element to be enabled
     */
    public void waitForEnabled(String selector, int timeout) {
        Locator locator = page.locator(selector);
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.VISIBLE));
        } catch (TimeoutError e) {
            throw new RuntimeException("Element not enabled after " + timeout + " ms: " + selector);
        }
    }

    /**
     * Wait for element to be enabled with default timeout
     */
    public void waitForEnabled(String selector) {
        waitForEnabled(selector, (int) DEFAULT_TIMEOUT);
    }

    // ==================== Text/Content Waits ====================

    /**
     * Wait for text to be present in element
     */
    public void waitForText(String selector, String expectedText, int timeout) {
        try {
            page.locator(selector).filter(new Locator.FilterOptions().setHasText(expectedText))
                    .waitFor(new Locator.WaitForOptions()
                            .setTimeout(timeout)
                            .setState(WaitForSelectorState.VISIBLE));
        } catch (TimeoutError e) {
            throw new RuntimeException("Text '" + expectedText + "' not found in element after " + timeout + " ms: " + selector);
        }
    }

    /**
     * Wait for text to be present with default timeout
     */
    public void waitForText(String selector, String expectedText) {
        waitForText(selector, expectedText, (int) DEFAULT_TIMEOUT);
    }

    // ==================== Element Count Waits ====================

    /**
     * Wait for element count to match expected value
     */
    public void waitForElementCount(String selector, int expectedCount, int timeout) {
        try {
            page.waitForFunction(
                    "([sel, count]) => document.querySelectorAll(sel).length === count",
                    new Object[]{selector, expectedCount},
                    new Page.WaitForFunctionOptions().setTimeout(timeout));
        } catch (TimeoutError e) {
            int actualCount = page.locator(selector).count();
            throw new RuntimeException("Element count did not match. Expected: " + expectedCount +
                    ", Actual: " + actualCount + " after " + timeout + " ms");
        }
    }

    // ==================== Custom Condition Waits ====================

    /**
     * Custom wait condition using a lambda
     */
    public void waitForCondition(java.util.function.Supplier<Boolean> condition, int timeout, String errorMessage) {
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < timeout) {
            if (condition.get()) {
                return;
            }
            sleep((int) DEFAULT_POLL_INTERVAL, TimeUnit.MILLISECONDS);
        }
        throw new RuntimeException(errorMessage + " after " + timeout + " ms");
    }

    // ==================== Utility Methods ====================

    /**
     * Sleep for specified duration
     */
    public static void sleep(int timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Sleep interrupted", e);
        }
    }
}