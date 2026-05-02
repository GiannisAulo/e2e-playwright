package pages.portal;

import com.microsoft.playwright.Page;
import utils.Utils;

public class PortalSelectorPage {
    private final Page page;
    private final Utils utils;
    private static final String TRADER_PORTAL_BTN = "#trader-portal-btn";
    private static final String NCA_PORTAL_BTN = "#nca-portal-btn";
    private static final String PORTAL_SELECTOR = "#portal-selector";

    public PortalSelectorPage(Page page, Utils utils) {
        this.page = page;
        this.utils = utils;
    }

    public TraderLoginPage enterTraderPortal() {
        page.locator(TRADER_PORTAL_BTN).click();
        return new TraderLoginPage(page);
    }

    public NCALoginPage enterNCAPortal() {
        page.locator(NCA_PORTAL_BTN).click();
        return new NCALoginPage(page, utils);
    }

    public boolean isDisplayed() {
        return page.locator(PORTAL_SELECTOR).isVisible();
    }
}
