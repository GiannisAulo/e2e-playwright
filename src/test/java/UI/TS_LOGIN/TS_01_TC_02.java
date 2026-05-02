package UI.TS_LOGIN;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.login.LoginPage;
import pages.pageObject.Base;

@Epic("Practice Portal")
@Feature("Portal Selector")
public class TS_01_TC_02 extends Base {

    @Test
    @Story("Enter Trader Portal from selector")
    public void TS_01_TC_02() {
        step("1. Open test-page.html in a browser.")
                .ui().loginPage().navigate();
        step("2. Click \"Enter Trader Portal\"")
                .ui().loginPage().clickPortalCard(LoginPage.PortalCard.TRADER);
        step("  Expected:\n" +
                "    - The Trader portal section appears.\n" +
                "    - The Trader login form is visible.\n" +
                "    - The portal selector is hidden.")
                .ui().loginPage().verifyLoginCards(false);
        utils.ui().loginPage().verifyLoginTitle(LoginPage.PortalCard.TRADER, getTestCaseData(1));
    }
}
