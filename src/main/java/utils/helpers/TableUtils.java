package utils.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import config.TestDataConfig;
import org.testng.Assert;
import utils.browserControls.BrowserControls;
import utils.elements.Elements;
import utils.enums.Actions;
import utils.waits.WaitUtils;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

public class TableUtils {
    private final Page page;
    private final TestDataConfig testDataConfig;
    private final Elements elements;
    private final WaitUtils waitUtils;
    private final BrowserControls browserControls;

    public TableUtils(Page page) {
        this.page = page;
        testDataConfig = new TestDataConfig();
        elements = new Elements(page);
        waitUtils = new WaitUtils(page);
        browserControls = new BrowserControls(page);
    }

    public void verifyTableValues(String tableId, List<List<String>> expectedData) {
        int rowCount = page.locator(tableId + " tr").count() - 1; // exclude header
        Assert.assertEquals(rowCount, expectedData.size(),
                "Row count mismatch!");

        for (int i = 0; i < expectedData.size(); i++) {
            List<String> actualRow = page.locator(tableId + " tr").nth(i + 1)
                    .locator("td").allTextContents();
            List<String> expectedRow = expectedData.get(i);

            Assert.assertEquals(actualRow.size(), expectedRow.size(),
                    "Column count mismatch in row " + (i + 1));

            for (int j = 0; j < expectedRow.size(); j++) {
                Assert.assertEquals(actualRow.get(j).trim(), expectedRow.get(j).trim(),
                        String.format("Mismatch at row %d, col %d", i + 1, j + 1));
            }
        }
    }

    public void verifyColumnLabels(String tableId, List<String> expectedHeaders) {
        List<String> actualHeaders = page.locator(tableId + " th").allTextContents();
        Assert.assertEquals(actualHeaders.size(), expectedHeaders.size(),
                "Column count mismatch!");

        for (int i = 0; i < expectedHeaders.size(); i++) {
            Assert.assertEquals(actualHeaders.get(i).trim(), expectedHeaders.get(i).trim(),
                    "Mismatch at column index " + i);
        }
    }

    /**
     * Example: verify if a specific cell contains text.
     */
    public void verifyCellContains(String tableSelector, int row, int column, String expectedText) {
        String actual = page.locator(tableSelector + " tr").nth(row)
                .locator("td").nth(column).innerText().trim();
        Assert.assertTrue(actual.contains(expectedText),
                "Expected text '" + expectedText + "' not found in cell [" + row + "," + column + "]");
    }

    public void verifyTableIsEmpty(String tableId) {
        int rowCount = page.locator(tableId + " tbody tr").count();
        Assert.assertEquals(rowCount, 0, "Table is not empty !!!");
    }

    public int getTableRowCount(String tableId) {
        return page.locator(tableId + " tbody tr").count();
    }

    public Locator getTableRows(String tableId) {
        return page.locator(tableId + " tbody tr");
    }


    //TODO: fix this
    public void verifyEntry(Properties params, String tableId) {
        List<String> expectedValues = testDataConfig.getMultipleStringParameters(params, "RowValues");

        if (expectedValues.isEmpty()) {
            throw new Error("Row values are not provided in params file");
        }

        String keyValue = expectedValues.get(0);

        Locator targetRow = page.locator(tableId + " tbody tr")
                .filter(new Locator.FilterOptions().setHasText(keyValue))
                .first();

        Assert.assertTrue(targetRow.count() > 0,
                "Row with key value '" + keyValue + "' not found in table");

        Locator cells = targetRow.locator("td");

        for (int i = 0; i < expectedValues.size(); i++) {
            String actualValue = cells.nth(i).textContent().trim();
            String expectedValue = expectedValues.get(i);

            Assert.assertTrue(actualValue.contains(expectedValue),
                    String.format("Cell %d does not contain expected value. Expected: '%s', Actual: '%s'",
                            i, expectedValue, actualValue));
        }
    }

    public void verifySortingIsPresent(String tableID, Properties params) {
        List<String> sortingColumnNames = testDataConfig.getMultipleStringParameters(params, "Sorting_Title");
        sortingColumnNames.forEach(column -> {
            String xpath = String.format("//*[@id='%s']//th[contains(.,'%s')]//span", tableID, column);
            boolean visible = elements.button().isButtonVisible(xpath);
            Assert.assertTrue(visible, "Sorting icon not visible for column: " + column);
        });
    }

    /**
     * This method is finding dynamically the sequence of the column in a table
     */
    private int getPTableColumnSequence(String tableId, String columnName) {
        Locator table = page.locator(tableId);
        Locator headers = table.locator("thead th");

        int count = headers.count();
        for (int i = 0; i < count; i++) {
            String headerText = headers.nth(i).textContent().trim();
            if (headerText.equals(columnName)) {
                return i; // Returns 0-based index
            }
        }

        throw new NoSuchElementException("Column '" + columnName + "' not found in table");
    }

    /**
     * Verify that all the registries have the specified Actions available
     *
     * @param tableID            The table id
     * @param expectedVisibility Whether the actions should be visible or not
     * @param actions            The actions to verify (varargs)
     */
    public void verifyAvailableActionsOnTable(String tableID, boolean expectedVisibility, Actions... actions) {
        int actionsColumn = getPTableColumnSequence(tableID, "Actions");
        int cssColumnIndex = actionsColumn + 1;

        page.locator("eui-dropdown + div.eui-table-paginator__page-range").waitFor();
        String totalRecords = page.locator("eui-dropdown + div.eui-table-paginator__page-range").textContent().trim();
        totalRecords = totalRecords.substring(totalRecords.lastIndexOf(" ")).trim();

        int tableSize = 0;
        do {
            tableSize += getTableRowCount(tableID);
            int currentRow = 1;
            do {
                for (Actions action : actions) {
                    String actionIdentifier = action.getIdentifier();

                    String cssSelector = String.format(
                            "table#%s tbody tr:nth-child(%d) td:nth-child(%d) button[aria-label*='%s' i], " +
                                    "table#%s tbody tr:nth-child(%d) td:nth-child(%d) button[title*='%s' i]",
                            tableID, currentRow, cssColumnIndex, actionIdentifier,
                            tableID, currentRow, cssColumnIndex, actionIdentifier
                    );

                    Locator actionButton = page.locator(cssSelector);
                    boolean visibility = actionButton.count() > 0 && actionButton.isVisible();

                    Assert.assertEquals(visibility, expectedVisibility,
                            "Row " + currentRow + ": The " + action.name() + " Action should " + (expectedVisibility ? "be" : "not be") + " available.");
                }
                currentRow++;
            }
            while (currentRow <= getTableRowCount(tableID));

            if (isGoToNextPageButtonEnabled(tableID))
                goToNextPage(tableID);
        } while (tableSize < Integer.parseInt(totalRecords));
    }

    public Boolean isGoToFirstPageButtonEnabled(String tableID) {
        String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to first page' i])", tableID);
        return page.locator(cssSelector).isEnabled();
    }

    public Boolean isGoToPreviousPageButtonEnabled(String tableID) {
        String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to previous page' i])", tableID);
        return page.locator(cssSelector).isEnabled();
    }

    public Boolean isGoToNextPageButtonEnabled(String tableID) {
        String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to next page' i])", tableID);
        return page.locator(cssSelector).isEnabled();
    }

    public Boolean isGoToLastPageButtonEnabled(String tableID) {
        String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to last page' i])", tableID);
        return page.locator(cssSelector).isEnabled();
    }

    public void goToNextPage(String tableID) {
        if (isGoToNextPageButtonEnabled(tableID)) {
            String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to next page' i])", tableID);
            page.locator(cssSelector).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } else {
            System.out.println("ERROR: Next page button is NOT enabled.");
        }
    }

    public void goToLastPage(String tableID) {
        if (isGoToLastPageButtonEnabled(tableID)) {
            String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to last page' i])", tableID);
            page.locator(cssSelector).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } else {
            System.out.println("ERROR: Last page button is NOT enabled.");
        }
    }

    public void goToPreviousPage(String tableID) {
        if (isGoToPreviousPageButtonEnabled(tableID)) {
            String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to previous page' i])", tableID);
            page.locator(cssSelector).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } else {
            System.out.println("ERROR: Previous page button is NOT enabled.");
        }
    }

    public void goToFirstPage(String tableID) {
        if (isGoToFirstPageButtonEnabled(tableID)) {
            String cssSelector = String.format("table#%s ~ eui-table-paginator button:has(span[aria-label*='Go to first page' i])", tableID);
            page.locator(cssSelector).click();
            page.waitForLoadState(LoadState.NETWORKIDLE);
        } else {
            System.out.println("ERROR: First page button is NOT enabled.");
        }
    }

    /**
     * Verifies that the user can move through the table by clicking the pagination buttons
     */
    public void verifyFunctionalityOfPaginationButtons(String tableID) {
        waitUtils.waitForVisible(tableID);
        goToFirstPage(tableID);
        goToNextPage(tableID);
        goToPreviousPage(tableID);
        goToLastPage(tableID);
    }

    private void verifyLimitationOfTableRows(int expectedRows, String tableID) {
        waitUtils.waitForVisible(tableID, 5000);
        int actualRows = getTableRowCount(tableID);
        Assert.assertTrue(expectedRows < actualRows,
                "The table rows exceed the limitation" + "of maximum displayed results. " +
                        "The results should be equals or less than " + actualRows + " but was "
                        + expectedRows);
    }

    /**
     * Verifies the pagination and limitation of displayed results
     *
     * @param tableID    The element id of table
     * @param limitation The number of pagination
     */
    public void verifyPaginationAndLimitation(String tableID, int limitation) {
        Locator limitationDropdown = page.locator("#" + tableID + " ~ eui-dropdown");
        browserControls.scrollElementIntoView(limitationDropdown);
        limitationDropdown.click();

        waitUtils.waitForVisible("//following::button[@role='listitem']/div[text()='" + limitation + "']");
        limitationDropdown.locator("//following::button[@role='listitem']/div[text()='" + limitation + "']").click();
        String totalRecords = elements.getText("//eui-dropdown//following::div[contains(@class,'eui-table-paginator__page-range')]");

        totalRecords = totalRecords.substring(totalRecords.lastIndexOf(" ")).trim();
        int intTotalRecords = Integer.parseInt(totalRecords);
        if (intTotalRecords < 10) {
            totalRecords = totalRecords.substring(totalRecords.lastIndexOf("of ") + 1).trim();
            intTotalRecords = Integer.parseInt(totalRecords);
        }
        int numOfRows = 0;
        int rest;
        do {

            int actualNumOfRows = getTableRowCount(tableID);
            rest = intTotalRecords - actualNumOfRows - numOfRows;

            if (rest != 0)
                Assert.assertEquals(actualNumOfRows, limitation, "The table rows exceed the limitation " +
                        "of maximum displayed results. The results should be equals or less than " + limitation +
                        " but was " + actualNumOfRows);
            else
                verifyLimitationOfTableRows(limitation, tableID);
            numOfRows = numOfRows + actualNumOfRows;

            if (isGoToNextPageButtonEnabled(tableID))
                goToNextPage(tableID);
        }
        while (rest != 0);
        goToFirstPage(tableID);
    }

    /**
     * Verifies the availability of pagination buttons
     */
    public void verifyAvailabilityOfPaginationButtons(String tableID) {
        goToFirstPage(tableID);

        String firstPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to first page'])", "disabled");
        Assert.assertTrue(Boolean.parseBoolean(firstPageDisabled));

        String previousPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to previous page'])", "disabled");
        Assert.assertTrue(Boolean.parseBoolean(previousPageDisabled));

        String nextPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to next page'])", "disabled");
        Assert.assertFalse(Boolean.parseBoolean(nextPageDisabled));

        String lastPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to last page'])", "disabled");
        Assert.assertFalse(Boolean.parseBoolean(lastPageDisabled));

        goToLastPage(tableID);

        firstPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to first page'])", "disabled");
        Assert.assertFalse(Boolean.parseBoolean(firstPageDisabled));

        previousPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to previous page'])", "disabled");
        Assert.assertFalse(Boolean.parseBoolean(previousPageDisabled));

        nextPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to next page'])", "disabled");
        Assert.assertTrue(Boolean.parseBoolean(nextPageDisabled));

        lastPageDisabled = page.getAttribute("#" + tableID + " ~ eui-table-paginator button:has(span[aria-label*='Go to last page'])", "disabled");
        Assert.assertTrue(Boolean.parseBoolean(lastPageDisabled));
    }
}
