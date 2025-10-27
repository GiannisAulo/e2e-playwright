package utils.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import config.TestDataConfig;
import org.testng.Assert;
import utils.elements.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VerificationUtils {
    private final TestDataConfig testDataConfig = new TestDataConfig();
    private final Page page;
    private final Elements elements;

    public VerificationUtils(Page page) {
        this.page = page;
        elements = new Elements(this.page);
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

    public void verifyInputRedHighlightedColor(By by) {
        WebElement webElement = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(by);
        String color = webElement.getCssValue("border");
        if (color.equals("rgb(218, 33, 49)") || color.equals("red")) {
            System.out.println("The input is highlighted as red.");
        } else {
            System.out.println("The input is not highlighted as red.");
        }
    }

    /**
     * Verify that all the registries have the Edit and Delete Actions available
     *
     * @param tableID The table id
     */
    public void verifyAvailableActionsOnTable(String tableID, boolean expectedVisibility) {
        wait.waitForLoad();
        int actionsColumn = table.getPTableColumnSequence(tableID, "Actions");
        wait.forElementToBePresent(By.xpath("//eui-dropdown//following::div[contains(@class,'eui-table-paginator__page-range')]"));
        String totalRecords = utils.webDriverFactory().getDriver().findElement(By.xpath("//eui-dropdown//following::div[contains(@class,'eui-table-paginator__page-range')]")).getText();
        totalRecords = totalRecords.substring(totalRecords.lastIndexOf(" ")).trim();
        int tableSize = 0;
        do {
            tableSize += table.getPTableBodyRows(tableID);
            int currentRow = 1;
            do {
                boolean visibilityOfEdit = isElementPresent(By.xpath(
                        "//table[@id='" + tableID + "']//tbody//tr[" + currentRow + "]/td[" + actionsColumn + "]" +
                                "//button"));
                Assert.assertEquals(visibilityOfEdit, expectedVisibility, "The Edit Action should be available.");
                boolean visibilityOfDelete = isElementPresent(By.xpath(
                        "//table[@id='" + tableID + "']//tbody//tr[" + currentRow + "]/td[" + actionsColumn + "]" +
                                "//button"));
                Assert.assertEquals(visibilityOfDelete, expectedVisibility, "The Delete Action should be available.");
                currentRow++;
            }
            while (currentRow <= table.getPTableBodyRows(tableID));
            if (table.isGoToNextPageButtonEnabled(tableID))
                table.goToNextPage(tableID);
            wait.waitForLoad();
        } while (tableSize < Integer.parseInt(totalRecords));
    }

    /**
     * Verifies the pagination and limitation of displayed results
     *
     * @param tableID    The element id of table
     * @param limitation The number of pagination
     */
    public void verifyPaginationAndLimitation(String tableID, int limitation) {
        WebElement pDropdown = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver()
                .findElement(By.xpath(".//*[@id='" + tableID + "']//following::eui-dropdown"));
        pDropdown.click();
        browserControls.scrollToElement(pDropdown);
        wait.forElementToBePresent(By.xpath("//following::button[@role='listitem']/div[text()='" + limitation + "']"));
        pDropdown.findElement(By.xpath("//following::button[@role='listitem']/div[text()='" + limitation + "']"))
                .click();
        wait.waitForLoad();
        wait.sleep(2, TimeUnit.SECONDS);
        wait.forElementToBePresent(By.id(tableID));
        String totalRecords = utils.webDriverFactory().getDriver().findElement(
                By.xpath("//eui-dropdown//following::div[contains(@class,'eui-table-paginator__page-range')]")).getText();
        totalRecords = totalRecords.substring(totalRecords.lastIndexOf(" ")).trim();
        int intTotalRecords = Integer.parseInt(totalRecords);
        if (intTotalRecords < 10) {
            totalRecords = totalRecords.substring(totalRecords.lastIndexOf("of ") + 1).trim();
            intTotalRecords = Integer.parseInt(totalRecords);
        }
        int numOfRows = 0;
        int rest;
        do {

            int actualNumOfRows = table.getPTableBodyRows(tableID);
            rest = intTotalRecords - actualNumOfRows - numOfRows;

            if (rest != 0)
                Assert.assertEquals(actualNumOfRows, limitation, "The table rows exceed the limitation " +
                        "of maximum displayed results. The results should be equals or less than " + limitation +
                        " but was " + actualNumOfRows);
            else
                verifyLimitationOfTableRows(actualNumOfRows, limitation);
            numOfRows = numOfRows + actualNumOfRows;

            if (table.isGoToNextPageButtonEnabled(tableID))
                table.goToNextPage(tableID);
        }
        while (rest != 0);
        table.goToFirstPage(tableID);
    }

    /**
     * Verifies that the user can move through the table by clicking the pagination buttons
     */
    public void verifyFunctionalityOfPaginationButtons(String tableID) {
        wait.waitForLoad();
        table.goToFirstPage(tableID);
        table.goToNextPage(tableID);
        table.goToPreviousPage(tableID);
        table.goToLastPage(tableID);
    }

    /**
     * Verifies the availability of pagination buttons
     */
    public void verifyAvailabilityOfPaginationButtons(String tableID) {
        table.goToFirstPage(tableID);
        String firstPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to first page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertTrue(Boolean.parseBoolean(firstPageDisabled));
        String previousPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to previous page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertTrue(Boolean.parseBoolean(previousPageDisabled));
        String nextPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to next page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertFalse(Boolean.parseBoolean(nextPageDisabled));
        String lastPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to last page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertFalse(Boolean.parseBoolean(lastPageDisabled));
        table.goToLastPage(tableID);
        firstPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to first page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertFalse(Boolean.parseBoolean(firstPageDisabled));
        previousPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to previous page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertFalse(Boolean.parseBoolean(previousPageDisabled));
        nextPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to next page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertTrue(Boolean.parseBoolean(nextPageDisabled));
        lastPageDisabled = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                                "span[contains(@aria-label,'Go to last page')]//ancestor::button"))
                .getAttribute("disabled");
        Assert.assertTrue(Boolean.parseBoolean(lastPageDisabled));

    }

    public void verifyPaginationExists(String tableID) {
        System.out.println("~ Verifying the first page button exists");
        Assert.assertTrue(isElementPresent(By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to first page')]//ancestor::button")));

        System.out.println("~ Verifying the previous page button exists");
        Assert.assertTrue(isElementPresent(By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to previous page')]//ancestor::button")));

        System.out.println("~ Verifying the next page button exists");
        Assert.assertTrue(isElementPresent(By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to next page')]//ancestor::button")));

        System.out.println("~ Verifying the last page button exists");
        Assert.assertTrue(isElementPresent(By.xpath("//table[@id='" + tableID + "']//following::eui-table-paginator//" +
                "span[contains(@aria-label,'Go to last page')]//ancestor::button")));
    }

    public void verifyTheAvailableLimitationOptionsOfTable(String tableID) {
        List<String> expectedLimitationOptions = Stream.of("10", "20", "50", "75", "100").collect(Collectors.toList());
        WebElement pDropdown = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver()
                .findElement(By.xpath(".//*[@id='" + tableID + "']//following::eui-dropdown"));
        List<String> actualLimitationOptions = element.primeNG().dropdown().getsValuesFromDropdown(pDropdown);
        Assert.assertEquals(actualLimitationOptions, expectedLimitationOptions, "Incorrect available limitation options.");
    }

    public void verifyItemsPerPageControlExists() {
        System.out.println("Verifying items per page exists");
        Assert.assertTrue(isElementPresent(By.xpath("//div[contains(@class,'table-paginator__page-selector')]"))
                , "Element is not presenet");
    }

    /*
    Check that each column has the filter field
     */
    public void verifyFiltersVisibilityForEachColumn(String tableID) {
        int columnsNumber = table.getPTableColumnsCount(tableID);
        for (int i = 1; i <= columnsNumber; i++) {
            isButtonElementPresent(By.xpath("//table[@id='" + tableID + "']/thead/tr/th/input"));
        }
    }

    /**
     * Verify the correct uploading message is displayed
     *
     * @param success True in case on successfully uploading or false in other case
     */
    public void verifySuccessfullyMessage(boolean success, Properties params) {
        String expectedMessage = testDataConfig.getStringParameter(params, NOTIFICATION_MESSAGE);
        WebElement uploadingResultElement = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(By.xpath(" "));
        wait.waitForElement(uploadingResultElement);

        if (success) {
            Assert.assertEquals(uploadingResultElement.getText(), expectedMessage, "Incorrect uploading result.");
        } else {
            Assert.assertNotEquals(uploadingResultElement.getText(), expectedMessage, "Incorrect uploading result.");
        }
    }

    /**
     * Verify the Notification Alert is displayed
     */
    public void verifyConfirmationAlertIsDisplayed() {
        wait.forAlertToBePresent();
        com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().switchTo().alert();
    }

    public void verifyDisplayedModalMessage(Properties params) {
        String expectedMessage = params.getProperty("Message");
        verifyModalMessage(expectedMessage);
    }

    public void verifyInputErrorMessage(String errorMessage) {
        System.out.println("Verifying error message...");
        wait.forElementToBePresent(GeneralElements.ERROR_FEEDBACK_MESSAGE);
        Assert.assertEquals(element.getElement(GeneralElements.ERROR_FEEDBACK_MESSAGE).getText().trim(), errorMessage);
    }


    public void verifyDisplayedModalMessage(String expectedMessage) {
        verifyModalMessage(expectedMessage);
    }

    private void verifyModalMessage(String expectedMessage) {
        wait.waitForElement(element.getElement(By.xpath("//div[@role='dialog']")));
        if (isElementPresent(By.xpath("//div[@role='dialog']//div[contains(@class,'eui-dialog__body')]//p"))) {
            String actualMessage = element.getElement(By.xpath("//div[@role='dialog']//div[contains(@class,'eui-dialog__body')]//p")).getText();
            Assert.assertEquals(actualMessage, expectedMessage, "Incorrect displayed message.");
        } else {
            String actualMessage = element.getElement(By.xpath("(//div[@role='dialog']//div[contains(@class,'eui-dialog__body')]/div)[2]")).getText();
            Assert.assertEquals(actualMessage, expectedMessage, "Incorrect displayed message.");
        }
    }

    public void verifyAlertMessageInPopUp(String expectedInfoMessage) {
        wait.waitForElement(element.getElement(By.xpath("//div[@role='dialog']")));
        String info = element.getElementText(GeneralElements.MODAL_ALERT);
        Assert.assertEquals(info, expectedInfoMessage, "Wrong Alert message !!!");
    }

    public void verifyWarningMessage(String expectedWarning) {
        wait.waitForElement(element.getElement(GeneralElements.WARNING_ALERT));
        String actualWarning = element.getElementText(GeneralElements.WARNING_ALERT);
        Assert.assertEquals(actualWarning.trim(), expectedWarning.trim(), "Wrong Warning message !!!");
    }

    public void verifyWarningNotificationMessage(String expectedWarning) {
        wait.waitForElement(element.getElement(GeneralElements.WARNING_NOTIFICATION));
        String actualWarning = element.getElementText(GeneralElements.WARNING_NOTIFICATION);
        Assert.assertEquals(actualWarning.trim(), expectedWarning.trim(), "Wrong Warning message !!!");
    }

    public void verifyConfirmationDialogIsClosed() {
        wait.waitForInvisibilityOfElement(By.xpath("//div[@role='dialog']"));
        System.out.println("~ Verifying that Confirmation dialog is closed");
        Assert.assertFalse(isElementPresent(By.xpath("//div[@role='dialog']")), "Element is present");
    }

    public void verifyConfirmationDialog() {
        wait.waitForVisibilityOfElement(By.xpath("//div[@role='dialog' and @data-e2e='eui-dialog']"));
        System.out.println("~ Verifying that Confirmation dialog");
        Assert.assertTrue(isElementPresent(By.xpath("//div[@role='dialog' and @data-e2e='eui-dialog']")), "Are you sure you want to delete this emission?");
    }

    public Boolean verifyState(String tableID, QuarterlyReportPage.QUARTER quarter, Properties params) {
        String expectedState = params.getProperty(STATE);
        Map<String, String> reportDate = findQuarter(quarter.getLabel());
        String value = reportDate.get("quarter");
        int rowSize = table.getPTableBodyRows(tableID);
        int columnSequenceState = table.getPTableColumnSequence(tableID, "State");
        int columnSequenceQuarter = table.getPTableColumnSequence(tableID, "Quarter");
        int rowSequence = 1;
        for (int i = 1; i <= rowSize; i++) {
            String rowRegistry = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                            By.xpath("//table[@id='" + tableID + "']//tbody//tr[" + i + "]/td[" + columnSequenceQuarter + "]/span"))
                    .getText();
            if (rowRegistry.equals(value)) {
                rowSequence = i;
                break;
            }
        }
        Assert.assertEquals(com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(
                        By.xpath("//table[@id='" + tableID + "']//tbody//tr[" + rowSequence + "]/td[" + columnSequenceState + "]/span"))
                .getText(), expectedState, "Wrong state");
        return true;
    }

    private void verifyLimitationOfTableRows(int actualNumOfRows, int maximumRows) {
        Assert.assertTrue(actualNumOfRows < maximumRows,
                "The table rows exceed the limitation" + "of maximum displayed results. " +
                        "The results should be equals or less than " + maximumRows + " but was "
                        + actualNumOfRows);
    }

    public static void isElementEnabled(WebElement webElement, String errorMessage) {
        if (webElement.getTagName().equals("p-dropdown")) {
            Assert.assertFalse(webElement.findElement(By.xpath("./div")).getAttribute("class").contains("p-disable"),
                    "Field is not Enabled");
        } else if (webElement.getTagName().equals("ux-datepicker")) {
            Assert.assertNull(webElement.getAttribute("isdisabled"), "Field is not Enabled");
        } else {
            Assert.assertTrue(webElement.isEnabled(), errorMessage);
        }
    }

    public boolean isElementDisabled(By locator) {
        WebElement webElement = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(locator);
        return !webElement.isEnabled();
    }

    public boolean isElementEnabled(By locator) {
        WebElement webElement = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver().findElement(locator);
        return webElement.isEnabled();
    }

    public boolean isNotificationSuccessMessageDisplayed(String message) {
        boolean isDisplayed = false;
        try {
            List<WebElement> notifications = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver()
                    .findElements(By.xpath("//div[@role='region']//div[contains(@class,'item-message')]/p"));
            for (WebElement notification : notifications) {
                String successMessage = notification.getText();
                if (successMessage.equals(message)) {
                    isDisplayed = true;
                    break;
                }
            }
            Assert.assertTrue(isDisplayed, "The message is not the expected.");
        } catch (Exception e) {
        }
        return isDisplayed;
    }

    public void assertNotificationMessages(Properties params) {
        List<String> notificationMessages = testDataConfig.getMultipleStringParameters(params, "notificationMessage");
        try {
            List<String> notificationTexts = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver()
                    .findElements(By.xpath("//p[contains(@class, 'eui-growl-item-message-detail')]//ul/li"))
                    .stream()
                    .map(WebElement::getText)
                    .collect(Collectors.toList());
            for (String expectedMessage : notificationMessages) {
                Assert.assertTrue(notificationTexts.contains(expectedMessage),
                        "Expected message not found: '" + expectedMessage + "' in notifications: " + notificationTexts);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("An error occurred while verifying the notifications: " + e.getMessage());
        }
    }

    public void verifyTabValidationErrorNotDisplayed(Properties params) {
        String error = params.getProperty(TAB_NAME_ERROR);
        Assert.assertFalse(isElementPresent(
                        By.xpath(" (//div[contains(@class,'eui-tab-item__label')][text()='" + error + "']//following::generic-error-length)[1]")),
                "No validation error");
    }

    public void verifyTabValidationErrorDisplayed(Properties params) {
        String error = params.getProperty(TAB_NAME_ERROR);
        Assert.assertTrue(isElementPresent(
                        By.xpath(" (//div[contains(@class,'eui-tab-item__label')][text()='" + error + "']//following::generic-error-length)[1]")),
                "No validation error");
    }

    public void verifyTotalGoodErrors(Properties params) {
        String errors = params.getProperty(TOTAL_GOOD_ERRORS);
        Assert.assertTrue(isElementPresent(By.xpath("(//div//generic-error-length/eui-badge)[1]")), "No validation error");
        Assert.assertEquals(element.getElement(By.xpath("(//div//generic-error-length/eui-badge)[1]")).getText(), errors);
    }

    public void verifyTotalEmissionErrors(Properties params) {
        String errors = params.getProperty(TOTAL_EMISSION_ERRORS);
        Assert.assertTrue(isElementPresent(By.xpath("//a[@class='navigation-title']/generic-error-length/eui-badge")), "No validation error");
        Assert.assertEquals(element.getElement(By.xpath("//a[@class='navigation-title']/generic-error-length/eui-badge")).getText(), errors);
    }

    public void verifyErrorFieldIsDisplayed(Properties params) {
        String errorFieldLabel = params.getProperty(ERROR_FIELD);
        Assert.assertTrue(isElementPresent(By.xpath("//label[contains(text(),'" + errorFieldLabel + "')]")));
    }

    public void verifyValidationScreenErrorCount(Properties params) {
        String errors = params.getProperty(SCREEN_ERRORS);
        Assert.assertTrue(isElementPresent(By.xpath("//div[@role='alert']//eui-badge")), "No validation error");
        Assert.assertEquals(element.getElement(By.xpath("//div[@role='alert']//eui-badge")).getText(), errors);
    }

    public void verifyScreenValidationErrorsMissing() {
        System.out.println("Verifying~ Validation errors are not displayed");
        Assert.assertFalse(isElementPresent(By.xpath("//eui-alert[contains(@class,'eui-alert--danger')]")),
                "Validation errors exist");
    }

    public void verifySpecificFieldValidationErrors(Properties params) {
        List<String> errors = testDataConfig.getMultipleStringParameters(params, SPECIFIC_ERRORS);
        List<String> actualErrors = new ArrayList<>();
        int errorsCount = new Element(utils).getElements(By.xpath("(//div[@role='alert']//li/a)")).size();
        for (int i = 1; i <= errorsCount; i++) {
            actualErrors.add(element.getElement(By.xpath("(//div[@role='alert']//li/a)[" + i + "]")).getText());
        }
        Collections.sort(errors);
        Collections.sort(actualErrors);
        Assert.assertEquals(actualErrors, errors, "Incorrect number of displayed errors");
    }

    public boolean verifyDownloadedFileExists(String fileName) {
        wait.sleep(5, TimeUnit.SECONDS);
        String userProfile = System.getenv("USERPROFILE");
        String path = userProfile + "\\Downloads";
        File dir = new File(path);
        File[] dirContents = dir.listFiles();
        boolean found = false;
        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                System.out.println("File Found !!!");
                dirContents[i].delete();
                found = true;
                return found;
            }
        }
        Assert.assertEquals(found, false, "File not found");
        return false;
    }

    /*
    Verify that the file is downloaded
     */
    public void verifyFileIsDownloaded(Properties params) throws IOException {
        Wait.sleep(5, TimeUnit.SECONDS);
//        BrowserControls browserControls1 =new BrowserControls(utils);
        String expectedFilename = params.getProperty(FILENAME);
        download.openDownloadsTab();
        String latestDownloadFileName = download.getLatestDownloadFileName();
        com.intrasoft.cbam.utils.Utils.closeCurrentHandle();
        com.intrasoft.cbam.utils.Utils.switchWindowHandle(0);
        Assert.assertTrue(Objects.requireNonNull(latestDownloadFileName).contains(expectedFilename),
                "The file is not downloaded. Expected: " + expectedFilename + " but found: " + latestDownloadFileName);
//        browserControls1.closeCurrentTab();
    }

    public boolean isExpanded(By by) {
        return new Element(utils).getElement(by).getAttribute("aria-label").equals("collapse");
    }

    public void verifyFileUploaded() {
        Assert.assertTrue(isElementPresent(By.xpath("//div[contains(@class,'file-upload__preview__filename')]")));
        Assert.assertTrue(isElementPresent(By.xpath("//div[contains(@class,'file-upload__preview__filesize')]")));
        Assert.assertTrue(isElementPresent(By.xpath("//div[contains(@class,'file-upload__preview__filetype')]")));
        Assert.assertTrue(isElementPresent(By.xpath("//div[contains(@class,'file-upload__total-size')]")));
    }

    public void verifyFileUploadedIsRemoved() {
        Assert.assertFalse(isElementPresent(By.xpath("//div[contains(@class,'file-upload__preview__filename')]")));
        Assert.assertFalse(isElementPresent(By.xpath("//div[contains(@class,'file-upload__preview__filesize')]")));
        Assert.assertFalse(isElementPresent(By.xpath("//div[contains(@class,'file-upload__preview__filetype')]")));
        Assert.assertFalse(isElementPresent(By.xpath("//div[contains(@class,'file-upload__total-size')]")));
    }

    public void verifyAvailableActionsOnTableUsingList(String tableID, boolean expectedVisibility, TABLETYPE tabletype) {
        List<String> availableActions = getListOfAvailableActionsBasedOnTable(tabletype);
        wait.waitForLoad();
        int actionsColumn = table.getPTableColumnSequence(tableID, "Actions");
        wait.forElementToBePresent(By.xpath("//eui-dropdown//following::div[contains(@class,'eui-table-paginator__page-range')]"));
        String totalRecords = utils.webDriverFactory().getDriver().findElement(By.xpath("//eui-dropdown//following::div[contains(@class,'eui-table-paginator__page-range')]")).getText();
        totalRecords = totalRecords.substring(totalRecords.lastIndexOf(" ")).trim();
        int tableSize = 0;
        do {
            tableSize += table.getPTableBodyRows(tableID);
            int currentRow = 1;
            do {
                for (String action : availableActions) {
                    boolean visibilityOfAction = isElementPresent(By.xpath(
                            "//table[@id='" + tableID + "']//tbody//tr[" + currentRow + "]/td[" + actionsColumn + "]" +
                                    "//button[@id='" + action + "']"));
                    Assert.assertEquals(visibilityOfAction, expectedVisibility,
                            "The " + action + " Action should be available for table " + tableID);
                }
                currentRow++;
            } while (currentRow <= table.getPTableBodyRows(tableID));

            if (table.isGoToNextPageButtonEnabled(tableID))
                table.goToNextPage(tableID);
            wait.waitForLoad();
        } while (tableSize < Integer.parseInt(totalRecords));
    }


}
