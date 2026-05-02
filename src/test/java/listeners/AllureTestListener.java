package listeners;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.factory.PlaywrightDriverFactory;

import java.io.ByteArrayInputStream;

public class AllureTestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        takeScreenshot(result.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        // method name serves as Allure test name by default
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        takeScreenshot(result.getName());
    }

    private void takeScreenshot(String testName) {
        try {
            Page page = PlaywrightDriverFactory.getPage();
            if (page != null && !page.isClosed()) {
                byte[] screenshot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
                Allure.addAttachment(testName + " — screenshot", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
            }
        } catch (Exception ignored) {
            // page may already be closed at teardown time
        }
    }

    @Override public void onStart(ITestContext context) {}
    @Override public void onFinish(ITestContext context) {}
    @Override public void onTestSuccess(ITestResult result) {}
}
