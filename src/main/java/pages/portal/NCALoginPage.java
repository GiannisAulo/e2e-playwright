package pages.portal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import utils.Utils;

public class NCALoginPage {
    private final Page page;
    private Utils utils;
    private static final String EMAIL_INPUT = "#nca-email";
    private static final String PASSWORD_INPUT = "#nca-password";
    private static final String LOGIN_BTN = "#nca-portal button.btn-primary";
    private static final String ERROR_MSG = "#nca-error";
    private static final String SUCCESS_MSG = "#nca-success";
    private static final String DASHBOARD = "#nca-dashboard";

    public NCALoginPage(Page page, Utils utils) {
        this.utils = utils;
        this.page = page;
    }

    public boolean isDisplayed() {
        return page.locator(DASHBOARD).isVisible();
    }

    public NCADashboardPage login(String email, String password) {
        page.locator(EMAIL_INPUT).fill(email);
        page.locator(PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BTN).click();
        page.locator(DASHBOARD).waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return new NCADashboardPage(page, this.utils);
    }

    public void attemptLogin(String email, String password) {
        page.locator(EMAIL_INPUT).fill(email);
        page.locator(PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BTN).click();
    }

    public String getErrorMessage() {
        return page.locator(ERROR_MSG).innerText().trim();
    }

    public boolean isErrorDisplayed() {
        return page.locator(ERROR_MSG).isVisible();
    }
}
