package pages.common.commonLocators;

/**
 * Common locators used across multiple pages
 * Converted from Selenium By locators to Playwright string selectors
 */
public class CommonLocators {

    // ==================== UPLOAD ELEMENTS ====================
    public static final String UPLOAD_SUBMIT = "//button[contains(@class,'button--primary')]/span[text()='Submit']";
    public static final String UPLOAD_ATTACHMENT_BUTTON = "//div[contains(@class,'upload')]";
    public static final String UPLOAD_ATTACHMENT_INPUT = "//eui-file-upload//input[@class='file-input']";
    public static final String UPLOAD_REMOVE_BUTTON = "//div[contains(@class,'file-upload__preview__actions')]/div/button";
    public static final String UPLOAD_CLOSE_BUTTON = "//button[contains(@class,'eui-button')]//span[contains(@class,'eui-icon-close')]";

    // ==================== CONFIRMATION DIALOGS ====================
    public static final String DECLINE_CONFIRMATION = "//button[contains(@class,'dismiss-button')]";
    public static final String ACCEPT_CONFIRMATION = "//button[contains(@class,'accept-button')]";
    public static final String DECLINE_CHANGES = "(//button[contains(@class,'dismiss-button')])[2]";
    public static final String ACCEPT_CHANGES = "(//button[contains(@class,'accept-button')])[2]";

    // ==================== MODAL/POPUP ELEMENTS ====================
    public static final String CLOSE_POPUP_MODAL = "//button//span[contains(@class,'eui-icon-close')]";
    public static final String CLICK_SOMEWHERE_ELSE = "//div[contains(@class,'transparent-backdrop')]";
    public static final String CONFIRMATION_MODAL = "//div[@role='dialog']";

    // ==================== FEEDBACK/ALERTS ====================
    public static final String ERROR_FEEDBACK_MESSAGE = "//eui-feedback-message[@type='MessageFeedback' and not(contains(@id, 'contactDetails-message'))]";
    public static final String MODAL_ALERT = "//eui-alert//div[@class='info-title']";
    public static final String WARNING_ALERT = "//div[@role='alert']";
    public static final String WARNING_NOTIFICATION = "//div//span[@class='eui-growl-item-message-title']";
    public static final String WARNING_MESSAGE = "//div[contains(@class,'eui-tab-content')]//span";

    // ==================== SCROLL BUTTONS ====================
    public static final String SCROLL_RIGHT_BUTTON = "//button[@aria-label='Scroll right']";
    public static final String SCROLL_LEFT_BUTTON = "//button[@aria-label='Scroll left']";
}