package utils.elements.button;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

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

    public boolean isButtonVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public boolean isButtonEnabled(String selector) {
        return page.locator(selector).isEnabled();
    }

    public void safeClick(String selector) {
        Locator locator = page.locator(selector);
        if (locator.isVisible() && locator.isEnabled()) {
            locator.click();
        } else {
            throw new IllegalStateException("Button is not clickable: not visible or disabled: " + selector);
        }
    }

    public boolean isButtonExpanded(String selector) {
        String ariaLabel = page.locator(selector).getAttribute("aria-label");
        return "collapse".equals(ariaLabel);
    }
}
