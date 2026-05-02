package UI.TS_LOGIN;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import pages.pageObject.Base;

@Epic("Practice Portal")
@Feature("Portal Selector")
public class TS_01_TC_01 extends Base {

    @Test
    @Story("Portal selector is shown on page load")
    public void TS_01_TC_01() {
        step("1. Open test-page.html in a browser.")
                .ui().loginPage().navigate();
        step("2. Verify the portal selector screen is displayed with two cards.")
                .ui().loginPage().verifyLoginCards(true);
    }
}
