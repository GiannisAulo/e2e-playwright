package pages.common.components;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.elements.Elements;

/**
 * Component for handling alerts, notifications, and feedback messages
 */
public class AlertComponent {
    private final Page page;
    private final Elements elements;

    private static final String ERROR_MESSAGE = "eui-feedback-message[type='MessageFeedback']:not([id*='contactDetails-message'])";
    private static final String MODAL_ALERT = "eui-alert div.info-title";
    private static final String WARNING_ALERT = "div[role='alert']";
    private static final String WARNING_NOTIFICATION = "div span.eui-growl-item-message-title";
    private static final String WARNING_MESSAGE = "div.eui-tab-content span";
    private static final String SUCCESS_MESSAGE = "div.success-message, .alert-success";
    private static final String INFO_MESSAGE = "div.info-message, .alert-info";

    public AlertComponent(Page page) {
        this.page = page;
        elements = new Elements(page);
    }

    // ==================== ERROR MESSAGES ====================

    /**
     * Check if error message is displayed
     *
     * @return true if error visible
     */
    public boolean isErrorDisplayed() {
        return elements.isElementVisible(ERROR_MESSAGE);
    }

    /**
     * Get error message text
     *
     * @return error message content
     */
    public String getErrorMessage() {
        return page.locator(ERROR_MESSAGE).textContent().trim();
    }

    /**
     * Wait for error message to appear
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForError(int timeout) {
        page.locator(ERROR_MESSAGE).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }

    // ==================== WARNING MESSAGES ====================

    /**
     * Check if warning is displayed
     *
     * @return true if warning visible
     */
    public boolean isWarningDisplayed() {
        return elements.isElementVisible(WARNING_ALERT);
    }

    /**
     * Get warning message text
     *
     * @return warning message content
     */
    public String getWarningMessage() {
        if (elements.isElementVisible(WARNING_NOTIFICATION)) {
            return page.locator(WARNING_NOTIFICATION).textContent().trim();
        }
        return page.locator(WARNING_ALERT).textContent().trim();
    }

    /**
     * Wait for warning to appear
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForWarning(int timeout) {
        page.locator(WARNING_ALERT).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }

    // ==================== MODAL ALERTS ====================

    /**
     * Get modal alert text
     *
     * @return modal alert content
     */
    public String getModalAlertText() {
        return page.locator(MODAL_ALERT).textContent().trim();
    }

    /**
     * Check if modal alert is displayed
     *
     * @return true if modal alert visible
     */
    public boolean isModalAlertDisplayed() {
        return elements.isElementVisible(MODAL_ALERT);
    }

    // ==================== SUCCESS MESSAGES ====================

    /**
     * Check if success message is displayed
     *
     * @return true if success visible
     */
    public boolean isSuccessDisplayed() {
        return elements.isElementVisible(SUCCESS_MESSAGE);
    }

    /**
     * Get success message text
     *
     * @return success message content
     */
    public String getSuccessMessage() {
        return page.locator(SUCCESS_MESSAGE).textContent().trim();
    }

    // ==================== INFO MESSAGES ====================

    /**
     * Check if info message is displayed
     *
     * @return true if info visible
     */
    public boolean isInfoDisplayed() {
        return elements.isElementVisible(INFO_MESSAGE);
    }

    /**
     * Get info message text
     *
     * @return info message content
     */
    public String getInfoMessage() {
        return page.locator(INFO_MESSAGE).textContent().trim();
    }

    // ==================== GENERIC ALERT METHODS ====================

    /**
     * Check if any alert is displayed
     *
     * @return true if any alert type is visible
     */
    public boolean isAnyAlertDisplayed() {
        return isErrorDisplayed() || isWarningDisplayed() ||
                isSuccessDisplayed() || isModalAlertDisplayed();
    }

    /**
     * Get any visible alert message
     *
     * @return first visible alert message
     */
    public String getAnyAlertMessage() {
        if (isErrorDisplayed()) return getErrorMessage();
        if (isWarningDisplayed()) return getWarningMessage();
        if (isSuccessDisplayed()) return getSuccessMessage();
        if (isModalAlertDisplayed()) return getModalAlertText();
        return "";
    }

    /**
     * Wait for any alert to disappear
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForAlertToDisappear(int timeout) {
        if (isAnyAlertDisplayed()) {
            page.waitForTimeout(timeout);
        }
    }
}