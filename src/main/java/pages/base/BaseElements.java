package pages.base;

import ui.pages.common.components.IPageElements;

/**
 * BaseElements holds common locators for all pages.
 * Locators are initialized in the constructor but are not passed as parameters.
 */
public class BaseElements implements IPageElements {
    protected final String tableId = "#defaultTable";
    protected final String headerLocator = "header.site-header";

    @Override
    public String getTableId() {
        return this.tableId;
    }

    @SuppressWarnings("unused")
    public String getHeaderLocator() {
        return headerLocator;
    }

}
