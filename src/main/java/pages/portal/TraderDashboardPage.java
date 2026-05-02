package pages.portal;

import com.microsoft.playwright.Page;
import utils.Utils;

public class TraderDashboardPage {
    private final Page page;
    Utils utils;
    private static final String DASHBOARD = "#trader-dashboard";
    private static final String USER_INFO = "#trader-user-info";
    private static final String LOGOUT_BTN = "#trader-logout-btn";
    private static final String SWITCH_PORTAL_BTN = "#trader-switch-btn";
    private static final String DECLARATIONS_TAB = "#tab-trader-declarations";
    private static final String INSTALLATIONS_TAB = "#tab-trader-installations";
    private static final String REPORT_TAB = "#tab-trader-report";
    private static final String CREATE_BTN = "#create-declaration-btn";
    private static final String DECLARATIONS_TBL = "#declarations-table";
    private static final String INSTALLATIONS_TBL = "#installations-table";

    public TraderDashboardPage(Page page) {
        this.utils = new Utils();
        this.page = page;
    }

    public boolean isDisplayed() {
        return page.locator(DASHBOARD).isVisible();
    }

    public String getLoggedInUser() {
        return page.locator(USER_INFO).innerText().trim();
    }

    public void clickDeclarationsTab() {
        page.locator(DECLARATIONS_TAB).click();
    }

    public void clickInstallationsTab() {
        page.locator(INSTALLATIONS_TAB).click();
    }

    public void clickReportTab() {
        page.locator(REPORT_TAB).click();
    }

    public int getDeclarationsRowCount() {
        return page.locator(DECLARATIONS_TBL + " tbody tr:not(.empty-row)").count();
    }

    public int getInstallationsRowCount() {
        return page.locator(INSTALLATIONS_TBL + " tbody tr:not(.empty-row)").count();
    }

    public boolean isCreateDeclarationButtonVisible() {
        return page.locator(CREATE_BTN).isVisible();
    }

    public void clickCreateDeclaration() {
        page.locator(CREATE_BTN).click();
    }

    public NCALoginPage switchToNCAPortal() {
        page.locator(SWITCH_PORTAL_BTN).click();
        return new NCALoginPage(page,this.utils);
    }

    public void logout() {
        page.locator(LOGOUT_BTN).click();
        // Confirm the logout dialog
        page.locator("#confirm-accept-btn").click();
    }
}
