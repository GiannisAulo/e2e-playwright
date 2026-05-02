package pages.login;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import config.EnvDataConfig;
import org.testng.Assert;
import utils.Utils;
import utils.elements.Elements;

import java.util.Properties;

public class LoginPage {
    private final Page page;
    private final Locator email;
    private final Locator password;
    private final Locator signIn;
    private final Elements elements;
    private Utils utils;

    private String traderPortalCard = "#trader-portal-card";
    private String traderPortalBtn = "#trader-portal-btn";
    private String ncaPortalCard = "#nca-portal-card";
    private String ncaPortalCardBtn = "#nca-portal-btn";

    public LoginPage(Page page, Utils utils) {
        this.page = page;
        this.email = page.locator("#login-email");
        this.password = page.locator("#login-password");
        this.signIn = page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Login"));
        this.elements = new Elements(page);
        this.utils = utils;
    }

    public void navigate() {
        page.navigate(new EnvDataConfig().getEnvProperties().getProperty("page.url"));
    }

    public void login(String email, String password) {
        this.navigate();
        this.email.fill(email);
        this.password.fill(password);
        signIn.click();
    }

    public void verifyLoginCards(boolean isVisible) {
        if (isVisible) {
            Assert.assertTrue(elements.isElementVisible(traderPortalCard));
            Assert.assertTrue(elements.isElementVisible(ncaPortalCard));
        } else {
            Assert.assertFalse(elements.isElementVisible(traderPortalCard));
            Assert.assertFalse(elements.isElementVisible(ncaPortalCard));
        }
    }

    public void verifyLoginTitle(PortalCard portalCard, Properties params) {
        String traderCardTitle = params.getProperty("TraderTitle");
        String ncaCardTitle = params.getProperty("NcaTitle");

        switch (portalCard) {
            case TRADER:
                Assert.assertEquals(traderCardTitle, elements.getText("#trader-login-section h2"));
                break;
            case NCA:
                Assert.assertEquals(ncaCardTitle, elements.getText("#nca-login-section h2"));
                break;
            default:
                throw new RuntimeException("Invalid PortalCard");
        }
    }

    public void clickPortalCard(PortalCard portalCard) {
        switch (portalCard) {
            case TRADER:
                elements.button().click(traderPortalCard);
                break;
            case NCA:
                elements.button().click(ncaPortalCard);
                break;
            default:
                throw new RuntimeException("Invalid PortalCard");
        }
    }

    public enum PortalCard {
        TRADER,
        NCA
    }
}
