package pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class LoginPage {
    private final Locator email;
    private final Locator password;
    private final Locator signIn;

    public LoginPage(Page page) {
        this.email = page.locator("#login-email");
        this.password = page.locator("#login-password");
        this.signIn = page.locator("//button[contains(text(),'Login')]");
    }

    public void login(String email, String password) {
        this.email.fill(email);
        this.password.fill(password);
        signIn.click();
    }
}
