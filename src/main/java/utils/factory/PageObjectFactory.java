package utils.factory;

import com.microsoft.playwright.Page;
import pages.login.LoginPage;
import pages.portal.NCADashboardPage;
import utils.Utils;

public class PageObjectFactory {
    private NCADashboardPage ncaDashboardPage;
    private LoginPage loginPage;
    Utils utils;
    Page page;

    public PageObjectFactory(Utils utils, Page page) {
        this.utils = utils;
        this.page = page;
    }

    public NCADashboardPage ncaDashboardPage() {
        return (ncaDashboardPage == null) ? ncaDashboardPage = new NCADashboardPage(page, utils) : ncaDashboardPage;
    }

    public LoginPage loginPage() {
        return (loginPage == null) ? loginPage = new LoginPage(page, utils) : loginPage;
    }
}
