package utils.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import config.TestDataConfig;
import org.testng.Assert;
import pages.common.commonLocators.CommonLocators;
import utils.elements.Elements;
import utils.waits.WaitUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VerificationUtils {
    private static final String TAB_NAME_ERROR = "TabNameError";
    private static final String TOTAL_GOOD_ERRORS = "TotalGoodErrors";
    private static final String TOTAL_EMISSION_ERRORS = "TotalEmissionErrors";
    private static final String ERROR_FIELD = "ErrorField";
    private static final String SCREEN_ERRORS = "ScreenErrors";
    private static final String SPECIFIC_ERRORS = "SpecificErrors";
    
    private static final String NOTIFICATION_MESSAGE = "NotificationMessage";

    private final TestDataConfig testDataConfig = new TestDataConfig();
    private final Page page;
    private final Elements elements;
    private final WaitUtils waitUtils;
    private final TableUtils table;

    public VerificationUtils(Page page) {
        this.page = page;
        elements = new Elements(this.page);
        waitUtils = new WaitUtils(this.page);
        table = new TableUtils(this.page);
    }

    public void verifyTooltipValue(String elementLocator, Properties params) {
        String expectedTooltip = params.getProperty("TooltipValue");
        Objects.requireNonNull(expectedTooltip, "Expected tooltip value not provided in params file");

        page.hover(elementLocator);
        Locator tooltip = page.locator("//div[contains(@class,'mat-tooltip eui-tooltip')]");
        tooltip.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        String actualTooltip = tooltip.innerText().trim();
        Assert.assertEquals(actualTooltip, expectedTooltip, "Wrong tooltip value!");
    }

    public void verifyVisibilityOfMenuItems(List<String> menuItems, boolean expectedVisibility) {
        if (!page.locator("//eui-sidebar[contains(@class, 'expanded')]").isVisible()) {
            page.click("eui-sidebar-toggle");
            page.waitForTimeout(300);
        }

        for (String menuItem : menuItems) {
            String menuItemXPath = "//span[contains(@class, 'eui-menu-item__label') and contains(.,'" + menuItem + "')]";
            Locator menuItemLocator = page.locator(menuItemXPath);

            boolean isVisible = menuItemLocator.isVisible();

            Assert.assertEquals(
                    isVisible,
                    expectedVisibility,
                    "Incorrect visibility for menu item: " + menuItem
            );
        }
    }

    public void verifyInputRedHighlightedColor(String inputElement) {
        Locator inputLocator = page.locator(inputElement);
        String borderColor = inputLocator.evaluate("el => window.getComputedStyle(el).borderColor").toString();
        boolean isRed = borderColor.equals("rgb(218, 33, 49)") ||
                borderColor.equals("red") ||
                borderColor.startsWith("rgb(218, 33, 49");

        if (!isRed) {
            throw new AssertionError("Expected border color to be red (rgb(218, 33, 49)), but was: " + borderColor);
        }
    }

    /**
     * Verifies the availability of pagination buttons
     */
    public void verifyAvailabilityOfPaginationButtons(String tableID) {
        table.verifyAvailabilityOfPaginationButtons(tableID);
    }

    public void verifyPaginationExists(String tableID) {
        System.out.println("~ Verifying the first page button exists");
        Assert.assertTrue(elements.isElementPresent("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to first page')]//ancestor::button"));

        System.out.println("~ Verifying the previous page button exists");
        Assert.assertTrue(elements.isElementPresent("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to previous page')]//ancestor::button"));

        System.out.println("~ Verifying the next page button exists");
        Assert.assertTrue(elements.isElementPresent("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to next page')]//ancestor::button"));

        System.out.println("~ Verifying the last page button exists");
        Assert.assertTrue(elements.isElementPresent("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to last page')]//ancestor::button"));
    }

    public void verifyTheAvailableLimitationOptionsOfTable(String tableID) {
        List<String> expectedLimitationOptions = List.of("10", "20", "50", "75", "100");
        Locator dropdown = page.locator("#" + tableID + " ~ eui-dropdown");
        dropdown.click();
        List<String> actualLimitationOptions = page.locator(".eui-dropdown__item").allInnerTexts()
                .stream().map(String::trim).toList();
        page.keyboard().press("Escape");
        Assert.assertEquals(actualLimitationOptions, expectedLimitationOptions, "Incorrect available limitation options.");
    }

    public void verifyItemsPerPageControlExists() {
        Assert.assertTrue(elements.isElementPresent("div.table-paginator__page-selector"), "Items per page control is not present");
    }

    public void verifyFiltersVisibilityForEachColumn(String tableID) {
        Locator headers = page.locator("#" + tableID + " thead tr th");
        int columnsNumber = headers.count();
        for (int i = 0; i < columnsNumber; i++) {
            Assert.assertTrue(
                    headers.nth(i).locator("input").isVisible(),
                    "Filter input not visible for column " + (i + 1));
        }
    }

    /**
     * Verify the correct uploading message is displayed
     *
     * @param success True in case on successfully uploading or false in other case
     */
    public void verifySuccessfullyMessage(boolean success, Properties params) {
        String expectedMessage = testDataConfig.getStringParameter(params, NOTIFICATION_MESSAGE);
        String uploadingResultLocator = "//div[contains(@class, 'eui-growl-item-message-detail')]"; 
        waitUtils.waitForVisible(uploadingResultLocator);

        String actualText = page.locator(uploadingResultLocator).innerText();
        if (success) {
            Assert.assertEquals(actualText, expectedMessage, "Incorrect uploading result.");
        } else {
            Assert.assertNotEquals(actualText, expectedMessage, "Incorrect uploading result.");
        }
    }

    /**
     * Verifies the feedback msg
     *
     * @param errorMessage
     */
    public void verifyInputErrorMessage(String errorMessage) {
        System.out.println("Verifying error message...");
        waitUtils.waitForVisible(CommonLocators.ERROR_FEEDBACK_MESSAGE);
        Assert.assertEquals(elements.getText(CommonLocators.ERROR_FEEDBACK_MESSAGE), errorMessage);
    }


    public void verifyDisplayedModalMessage(String expectedMessage) {
        verifyModalMessage(expectedMessage);
    }

    private void verifyModalMessage(String expectedMessage) {
        waitUtils.waitForElement(page.locator("//div[@role='dialog']"));
        if (elements.isElementPresent("//div[@role='dialog']//div[contains(@class,'eui-dialog__body')]//p")) {
            String actualMessage = page.locator("//div[@role='dialog']//div[contains(@class,'eui-dialog__body')]//p").innerText();
            Assert.assertEquals(actualMessage, expectedMessage, "Incorrect displayed message.");
        } else {
            String actualMessage = page.locator("(//div[@role='dialog']//div[contains(@class,'eui-dialog__body')]/div)[2]").innerText();
            Assert.assertEquals(actualMessage, expectedMessage, "Incorrect displayed message.");
        }
    }

    public void verifyAlertMessageInPopUp(String expectedInfoMessage) {
        waitUtils.waitForElement(page.locator("//div[@role='dialog']"));
        String info = page.locator(CommonLocators.MODAL_ALERT).innerText();
        Assert.assertEquals(info, expectedInfoMessage, "Wrong Alert message !!!");
    }

    public void verifyWarningMessage(String expectedWarning) {
        waitUtils.waitForElement(page.locator(CommonLocators.WARNING_ALERT));
        String actualWarning = page.locator(CommonLocators.WARNING_ALERT).innerText();
        Assert.assertEquals(actualWarning.trim(), expectedWarning.trim(), "Wrong Warning message !!!");
    }

    public void verifyWarningNotificationMessage(String expectedWarning) {
        waitUtils.waitForElement(page.locator(CommonLocators.WARNING_NOTIFICATION));
        String actualWarning = page.locator(CommonLocators.WARNING_NOTIFICATION).innerText();
        Assert.assertEquals(actualWarning.trim(), expectedWarning.trim(), "Wrong Warning message !!!");
    }

    public boolean isNotificationSuccessMessageDisplayed(String message) {
        List<String> notifications = page.locator("//div[@role='region']//div[contains(@class,'item-message')]/p").allInnerTexts();
        boolean isDisplayed = notifications.stream().anyMatch(message::equals);
        Assert.assertTrue(isDisplayed, "Notification message not found: '" + message + "' in: " + notifications);
        return isDisplayed;
    }

    public void assertNotificationMessages(Properties params) {
        List<String> notificationMessages = testDataConfig.getMultipleStringParameters(params, "notificationMessage");
        try {
            List<String> notificationTexts = page.locator("//p[contains(@class, 'eui-growl-item-message-detail')]//ul/li")
                    .allInnerTexts();
            for (String expectedMessage : notificationMessages) {
                Assert.assertTrue(notificationTexts.contains(expectedMessage),
                        "Expected message not found: '" + expectedMessage + "' in notifications: " + notificationTexts);
            }
        } catch (Exception e) {
            Assert.fail("An error occurred while verifying the notifications: " + e.getMessage());
        }
    }

    public void verifyTabValidationErrorNotDisplayed(Properties params) {
        String error = params.getProperty(TAB_NAME_ERROR);
        Assert.assertFalse(elements.isElementPresent(
                        " (//div[contains(@class,'eui-tab-item__label')][text()='" + error + "']//following::generic-error-length)[1]"),
                "No validation error");
    }

    public void verifyTabValidationErrorDisplayed(Properties params) {
        String error = params.getProperty(TAB_NAME_ERROR);
        Assert.assertTrue(elements.isElementPresent(
                        " (//div[contains(@class,'eui-tab-item__label')][text()='" + error + "']//following::generic-error-length)[1]"),
                "No validation error");
    }

    public void verifyTotalGoodErrors(Properties params) {
        String errors = params.getProperty(TOTAL_GOOD_ERRORS);
        Assert.assertTrue(elements.isElementPresent("(//div//generic-error-length/eui-badge)[1]"), "No validation error");
        Assert.assertEquals(page.locator("(//div//generic-error-length/eui-badge)[1]").innerText(), errors);
    }

    public void verifyTotalEmissionErrors(Properties params) {
        String errors = params.getProperty(TOTAL_EMISSION_ERRORS);
        Assert.assertTrue(elements.isElementPresent("//a[@class='navigation-title']/generic-error-length/eui-badge"), "No validation error");
        Assert.assertEquals(page.locator("//a[@class='navigation-title']/generic-error-length/eui-badge").innerText(), errors);
    }

    public void verifyErrorFieldIsDisplayed(Properties params) {
        String errorFieldLabel = params.getProperty(ERROR_FIELD);
        Assert.assertTrue(elements.isElementPresent("//label[contains(text(),'" + errorFieldLabel + "')]"));
    }

    public void verifyValidationScreenErrorCount(Properties params) {
        String errors = params.getProperty(SCREEN_ERRORS);
        Assert.assertTrue(elements.isElementPresent("//div[@role='alert']//eui-badge"), "No validation error");
        Assert.assertEquals(page.locator("//div[@role='alert']//eui-badge").innerText(), errors);
    }

    public void verifyScreenValidationErrorsMissing() {
        System.out.println("Verifying~ Validation errors are not displayed");
        Assert.assertFalse(elements.isElementPresent("//eui-alert[contains(@class,'eui-alert--danger')]"),
                "Validation errors exist");
    }

    public void verifySpecificFieldValidationErrors(Properties params) {
        List<String> errors = testDataConfig.getMultipleStringParameters(params, SPECIFIC_ERRORS);
        List<String> actualErrors = new ArrayList<>();
        int errorsCount = page.locator("(//div[@role='alert']//li/a)").count();
        for (int i = 1; i <= errorsCount; i++) {
            actualErrors.add(page.locator("(//div[@role='alert']//li/a)[" + i + "]").innerText());
        }
        Collections.sort(errors);
        Collections.sort(actualErrors);
        Assert.assertEquals(actualErrors, errors, "Incorrect number of displayed errors");
    }
}
