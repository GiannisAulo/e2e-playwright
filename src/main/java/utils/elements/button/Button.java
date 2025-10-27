package utils.elements.button;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.TimeoutError;
import com.microsoft.playwright.options.WaitForSelectorState;

public class Button {
    private final Page page;

    public Button(Page page) {
        this.page = page;
    }

    public void click(String selector) {
        page.locator(selector).click();
    }

    public void clickFirst(String selector) {
        page.locator(selector).first().click();
    }

    public void doubleClick(String selector) {
        page.locator(selector).dblclick();
    }

    public boolean isVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public boolean isEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    public void waitForVisible(String selector, int timeout) {
        Locator locator = page.locator(selector);
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setTimeout(timeout)
                    .setState(WaitForSelectorState.VISIBLE));
        } catch (TimeoutError e) {
            throw new RuntimeException("Button not visible after " + timeout + " ms: " + selector);
        }
    }

    public String getText(String selector) {
        return page.locator(selector).textContent();
    }

    public String getAttribute(String selector, String attributeName) {
        return page.locator(selector).getAttribute(attributeName);
    }

    public void hover(String selector) {
        page.locator(selector).hover();
    }

    public void scrollIntoView(String selector) {
        page.locator(selector).scrollIntoViewIfNeeded();
    }

    public void safeClick(String selector) {
        Locator locator = page.locator(selector);
        if (locator.isVisible() && locator.isEnabled()) {
            locator.click();
        } else {
            throw new IllegalStateException("Button is not clickable: not visible or disabled: " + selector);
        }
    }
}
