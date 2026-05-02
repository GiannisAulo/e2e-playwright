package pages.portal;

import com.microsoft.playwright.Page;
import utils.Utils;

public class NCADashboardPage {
    private final Page page;
    private final Utils utils;

    private static final String DASHBOARD = "#nca-dashboard";
    private static final String USER_INFO = "#nca-user-info";
    private static final String LOGOUT_BTN = "#nca-logout-btn";
    private static final String SWITCH_BTN = "#nca-switch-btn";
    private static final String REVIEW_TAB = "#tab-nca-review";
    private static final String OPERATORS_TAB = "#tab-nca-operators";
    private static final String AUDIT_TAB = "#tab-nca-audit";
    private static final String REVIEW_TABLE = "#review-table";
    private static final String OPERATORS_TABLE = "#operators-table";

    public NCADashboardPage(Page page,Utils utils) {
        this.utils = utils;
        this.page = page;
    }

    public boolean isDisplayed() {
        return page.locator(DASHBOARD).isVisible();
    }

    public String getLoggedInUser() {
        return page.locator(USER_INFO).innerText().trim();
    }

    public void clickReviewTab() {
        page.locator(REVIEW_TAB).click();
    }

    public void clickOperatorsTab() {
        page.locator(OPERATORS_TAB).click();
    }

    public void clickAuditTab() {
        page.locator(AUDIT_TAB).click();
    }

    public int getReviewQueueRowCount() {
        return page.locator(REVIEW_TABLE + " tbody tr:not(.empty-row)").count();
    }

    public int getOperatorsRowCount() {
        return page.locator(OPERATORS_TABLE + " tbody tr:not(.empty-row)").count();
    }

    public TraderLoginPage switchToTraderPortal() {
        page.locator(SWITCH_BTN).click();
        return new TraderLoginPage(page);
    }

    public void logout() {
        page.locator(LOGOUT_BTN).click();
        page.locator("#confirm-accept-btn").click();
    }
}
