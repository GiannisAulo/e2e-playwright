package pages.pageObject;

import com.microsoft.playwright.Page;

public class Tabs {
    protected Page page;

    public Tabs(Page page) {
        this.page = page;
    }

    private static final String loginTab = "#tab-login";
    private static final String formTab = "#tab-form";
    private static final String tableTab = "#tab-table";
    private static final String modalTab = "#modal-tab";

    public void clickTab(Tab tab) {
        switch (tab) {
            case login:
                page.locator(loginTab).click();
                break;
            case form:
                page.locator(formTab).click();
                break;
            case table:
                page.locator(tableTab).click();
                break;
            case modal:
                page.locator(modalTab).click();
                break;
            default:
                page.locator(loginTab).click();
        }
    }

    public enum Tab {
        login,
        form,
        table,
        modal
    }
}
