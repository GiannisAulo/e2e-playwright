package pages.portal;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TraderLoginPage {
    private final Page page;

    private static final String EMAIL_INPUT   = "#trader-email";
    private static final String PASSWORD_INPUT= "#trader-password";
    private static final String LOGIN_BTN     = "#trader-portal button.btn-primary";
    private static final String ERROR_MSG     = "#trader-error";
    private static final String SUCCESS_MSG   = "#trader-success";
    private static final String DASHBOARD     = "#trader-dashboard";

    public TraderLoginPage(Page page) {
        this.page = page;
    }

    public TraderDashboardPage login(String email, String password) {
        page.locator(EMAIL_INPUT).fill(email);
        page.locator(PASSWORD_INPUT).fill(password);
        page.locator(LOGIN_BTN).click();
        page.locator(DASHBOARD).waitFor(new Locator.WaitForOptions().setTimeout(5000));
        return new TraderDashboardPage(page);
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

    public boolean isSuccessDisplayed() {
        return page.locator(SUCCESS_MSG).isVisible();
    }
}
