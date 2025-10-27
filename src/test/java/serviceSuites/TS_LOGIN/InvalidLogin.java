package serviceSuites.TS_LOGIN;

import com.microsoft.playwright.Locator;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.pages.base.Base;
import ui.pages.login.LoginPage;

public class InvalidLogin extends Base {
    @Test
    public void InvalidLogin() {
        page.navigate("http://localhost:3000/test-page.html");
        String title = page.title();
        Assert.assertEquals(title.trim(), "Playwright Practice Page with Tabs & CRUD");
        LoginPage loginPage = new LoginPage(page);
        loginPage.login("admin@gmail.com","124567");
        Locator feedback = page.locator("#login-error");
        String feedbackText = feedback.innerText();
        Assert.assertEquals(feedbackText.trim(),"Incorrect email or password.");
    }
}
