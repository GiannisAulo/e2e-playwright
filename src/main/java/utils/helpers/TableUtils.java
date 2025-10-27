package utils.helpers;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import config.TestDataConfig;
import org.testng.Assert;
import utils.elements.Elements;

import java.util.List;
import java.util.Properties;

public class TableUtils {
    Page page;
    TestDataConfig testDataConfig;
    Elements elements;

    public TableUtils(Page page) {
        this.page = page;
        testDataConfig = new TestDataConfig();
        elements = new Elements(page);
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
        return page.locator(tableId + "tbody tr").count();
    }

    public Locator getTableRows(String tableId) {
        return page.locator(tableId + "tbody tr");
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
            boolean visible = elements.button().isVisible(xpath);
            Assert.assertTrue(visible, "Sorting icon not visible for column: " + column);
        });
    }


}
