package pages.pageObject;

import com.microsoft.playwright.Page;
import config.EnvDataConfig;
import config.RecoursesConfig;
import config.TestDataConfig;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.Utils;
import utils.factory.PlaywrightDriverFactory;

import java.util.Properties;

public class Base {
    protected static Page page;
    public static Utils utils;
    EnvDataConfig envDataConfig;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        envDataConfig = new EnvDataConfig();
        String browser = envDataConfig.getBrowser() != null ? envDataConfig.getBrowser() : "chromium";
        boolean headless = envDataConfig.getHeadlessMode();
        PlaywrightDriverFactory.init(browser, headless);
        page = PlaywrightDriverFactory.getPage();
        utils = new Utils();
    }

    public Utils step(String stepDescription) {
        Reporter.log(stepDescription, 0, true);
        return (utils == null) ? new Utils() : utils;
    }

    public Properties getTestCaseData(Integer instance) {
        String classPath = getFullPath();
        String testSuite = getTestSuiteName(classPath);
        String testCase = getTestCaseName(classPath);
        RecoursesConfig recoursesConfig = new RecoursesConfig();
        String propertyFile = recoursesConfig.getTestDataDir() + testSuite + ".properties";
        TestDataConfig testDataConfig = new TestDataConfig();
        return instance == null ? testDataConfig.getTestData(propertyFile, testCase) :
                testDataConfig.getTestData(propertyFile, testCase + "." + instance);
    }

    public String getTestCaseName(String classPath) {
        String testCase = null;
        for (String packageName : classPath.split("\\.")) {
            if (packageName.contains("_TC_")) {
                testCase = packageName;
                break;
            }
        }
        Assert.assertNotNull(testCase, "Test case name not found " + classPath);
        return testCase;
    }

    public String getTestSuiteName(String classPath) {
        String testSuite = null;
        for (String packageName : classPath.split("\\.")) {
            if (packageName.startsWith("TS_")) {
                testSuite = packageName;
                break;
            }
        }
        Assert.assertNotNull(testSuite, "TestSuite name not found" + classPath);
        return testSuite;
    }

    protected String getFullPath() {
        return getClass().getCanonicalName();
    }

    @AfterSuite(alwaysRun = true)
    public void teardown() {
        PlaywrightDriverFactory.close();
    }
}
