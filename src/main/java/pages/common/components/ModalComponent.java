package pages.common.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import utils.elements.Elements;

/**
 * Component for handling modal/popup operations
 */
public class ModalComponent {
    private final Page page;
    private final Elements elements;
    private static final String CLOSE_BUTTON = "button span[class*='eui-icon-close']";
    private static final String BACKDROP = "div[class*='transparent-backdrop']";
    private static final String MODAL = "div[role='dialog']";
    private static final String MODAL_CONTENT = "div[role='dialog'] .modal-content";
    private static final String MODAL_HEADER = "div[role='dialog'] .modal-header";
    private static final String ACCEPT_BUTTON = "button.accept-button";
    private static final String DECLINE_BUTTON = "button.dismiss-button";
    private static final String MODAL_TITLE = "div[role='dialog'] h2, div[role='dialog'] .dialog-title";
    private static final String MODAL_MESSAGE = "div[role='dialog'] .dialog-message, div[role='dialog'] p";

    public ModalComponent(Page page) {
        this.page = page;
        this.elements = new Elements(page);
    }

    /**
     * Close modal by clicking the X button
     */
    public void closeModal() {
        elements.button().clickFirst(CLOSE_BUTTON);
    }

    /**
     * Close modal by clicking outside (backdrop)
     */
    public void clickOutside() {
        elements.button().click(BACKDROP);
    }

    /**
     * Close modal using Escape key
     */
    public void closeWithEscape() {
        page.keyboard().press("Escape");
    }

    /**
     * Close any open modal (tries multiple methods)
     */
    public void closeAnyModal() {
        if (isModalVisible()) {
            try {
                closeModal();
            } catch (Exception e) {
                try {
                    clickOutside();
                } catch (Exception ex) {
                    closeWithEscape();
                }
            }
        }
    }

    // ==================== VERIFICATIONS ====================

    /**
     * Check if modal is visible
     *
     * @return true if visible
     */
    public boolean isModalVisible() {
        return elements.button().isButtonVisible(MODAL);
    }

    /**
     * Get modal content text
     *
     * @return modal content as string
     */
    public String getModalContent() {
        return page.locator(MODAL_CONTENT).textContent().trim();
    }

    /**
     * Get modal header text
     *
     * @return modal header as string
     */
    public String getModalHeader() {
        return page.locator(MODAL_HEADER).textContent().trim();
    }

    /**
     * Wait for modal to appear
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForModal(int timeout) {
        page.locator(MODAL).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }

    /**
     * Wait for modal to close
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForModalToClose(int timeout) {
        page.locator(MODAL).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeout));
    }

    /**
     * Wait for modal to close (default 5 seconds)
     */
    public void waitForModalToClose() {
        waitForModalToClose(5000);
    }

    /**
     * Accept/confirm the dialog (clicks first accept button)
     */
    public void acceptConfirmation() {
        elements.button().clickFirst(ACCEPT_BUTTON);
    }

    /**
     * Decline/dismiss the dialog (clicks first decline button)
     */
    public void declineConfirmation() {
        elements.button().clickFirst(DECLINE_BUTTON);
    }

    /**
     * Accept changes (clicks second accept button if multiple dialogs)
     */
    public void acceptChanges() {
        page.locator(ACCEPT_BUTTON).nth(1).click();
    }

    /**
     * Decline changes (clicks second decline button if multiple dialogs)
     */
    public void declineChanges() {
        page.locator(DECLINE_BUTTON).nth(1).click();
    }

    /**
     * Accept specific confirmation by index
     *
     * @param index - 0-based index of the button
     */
    public void acceptByIndex(int index) {
        page.locator(ACCEPT_BUTTON).nth(index).click();
    }

    /**
     * Decline specific confirmation by index
     *
     * @param index - 0-based index of the button
     */
    public void declineByIndex(int index) {
        page.locator(DECLINE_BUTTON).nth(index).click();
    }

    // ==================== VERIFICATIONS ====================

    /**
     * Check if confirmation dialog is visible
     *
     * @return true if visible
     */
    public boolean isConfirmationVisible() {
        return page.locator(MODAL).isVisible();
    }

    /**
     * Get the confirmation dialog title
     *
     * @return dialog title text
     */
    public String getConfirmationTitle() {
        return page.locator(MODAL_TITLE).textContent().trim();
    }

    /**
     * Get the confirmation dialog message
     *
     * @return dialog message text
     */
    public String getConfirmationMessage() {
        return page.locator(MODAL_MESSAGE).textContent().trim();
    }

    /**
     * Check if accept button is enabled
     *
     * @return true if enabled
     */
    public boolean isAcceptButtonEnabled() {
        return page.locator(ACCEPT_BUTTON).first().isEnabled();
    }

    /**
     * Wait for confirmation dialog to appear
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForConfirmation(int timeout) {
        page.locator(MODAL).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(timeout));
    }

    /**
     * Wait for confirmation dialog to disappear
     *
     * @param timeout - timeout in milliseconds
     */
    public void waitForConfirmationToClose(int timeout) {
        page.locator(MODAL).waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(timeout));
    }
}