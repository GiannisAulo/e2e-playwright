package pages.common.components;

import com.microsoft.playwright.Page;
import config.TestDataConfig;
import pages.base.BaseElements;
import utils.helpers.VerificationUtils;
import utils.elements.Elements;
import utils.helpers.TableUtils;

import java.util.List;
import java.util.Properties;

public abstract class TablePageComponent extends BaseElements {
    protected final Page page;
    protected final Elements elements;
    protected VerificationUtils verificationUtils;
    protected TableUtils tableUtils;

    public TablePageComponent(Page page) {
        this.page = page;
        this.elements = new Elements(page);
    }

    private TableUtils getTableInstance() {
        return (tableUtils == null) ? new TableUtils(page) : tableUtils;
    }

    private VerificationUtils getVerificationsInstance() {
        return (verificationUtils == null) ? new VerificationUtils(page) : verificationUtils;
    }

    /**
     * Verify that the table column headers match the expected values.
     */
    protected void verifyColumnLabels(Properties params) {
        List<String> expectedHeaders = TestDataConfig.multipleOrSingleTestData(params, "ColumnLabels");
        getTableInstance().verifyColumnLabels(getTableId(), expectedHeaders);
    }

    /**
     * Verify that the table data matches expected rows and columns.
     */
    protected void verifyTableValues(List<List<String>> expectedData) {
        getTableInstance().verifyTableValues(getTableId(), expectedData);
    }

    protected void tableIsEmpty() {
        getTableInstance().verifyTableIsEmpty(getTableId());
    }

}
