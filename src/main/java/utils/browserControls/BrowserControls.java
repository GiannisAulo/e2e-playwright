package utils.browserControls;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;


public final class BrowserControls {
    private final Page page;

    // CSS Locators
    private static final String SCROLL_RIGHT_BUTTON = "button[aria-label='Scroll right']";
    private static final String SCROLL_LEFT_BUTTON = "button[aria-label='Scroll left']";

    public BrowserControls(Page page) {
        this.page = page;
    }

    /**
     * Click scroll right button
     */
    public void scrollRight() {
        page.locator(SCROLL_RIGHT_BUTTON).click();
    }

    /**
     * Click scroll left button
     */
    public void scrollLeft() {
        page.locator(SCROLL_LEFT_BUTTON).click();
    }

    /**
     * Scroll right multiple times
     *
     * @param times - number of times to scroll
     */
    public void scrollRightMultiple(int times) {
        for (int i = 0; i < times; i++) {
            scrollRight();
            page.waitForTimeout(300); // Small delay between scrolls
        }
    }

    /**
     * Scroll left multiple times
     *
     * @param times - number of times to scroll
     */
    public void scrollLeftMultiple(int times) {
        for (int i = 0; i < times; i++) {
            scrollLeft();
            page.waitForTimeout(300);
        }
    }

    /**
     * Scroll element into view by selector
     *
     * @param selector - CSS selector of element
     */
    public void scrollElementIntoView(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    public void scrollElementIntoView(Locator selector) {
        selector.scrollIntoViewIfNeeded();
    }

    /**
     * Scroll to top of page
     */
    public void scrollToTop() {
        page.evaluate("window.scrollTo(0, 0)");
    }

    /**
     * Scroll to bottom of page
     */
    public void scrollToBottom() {
        page.evaluate("window.scrollTo(0, document.body.scrollHeight)");
    }

    /**
     * Scroll by specific pixels
     *
     * @param x - horizontal scroll amount
     * @param y - vertical scroll amount
     */
    public void scrollBy(int x, int y) {
        page.evaluate(String.format("window.scrollBy(%d, %d)", x, y));
    }

    // ==================== VERIFICATIONS ====================

    /**
     * Check if scroll right button is visible
     *
     * @return true if visible
     */
    public boolean isScrollRightVisible() {
        return page.locator(SCROLL_RIGHT_BUTTON).isVisible();
    }

    /**
     * Check if scroll left button is visible
     *
     * @return true if visible
     */
    public boolean isScrollLeftVisible() {
        return page.locator(SCROLL_LEFT_BUTTON).isVisible();
    }

    /**
     * Check if scroll right button is enabled
     *
     * @return true if enabled
     */
    public boolean isScrollRightEnabled() {
        return page.locator(SCROLL_RIGHT_BUTTON).isEnabled();
    }

    /**
     * Check if scroll left button is enabled
     *
     * @return true if enabled
     */
    public boolean isScrollLeftEnabled() {
        return page.locator(SCROLL_LEFT_BUTTON).isEnabled();
    }

}
