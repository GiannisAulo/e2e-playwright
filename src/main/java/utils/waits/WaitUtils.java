package utils.waits;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WaitUtils {
    private static final long DEFAULT_TIMEOUT = 10;
    private static final long DEFAULT_POLL_INTERVAL = 2;

    com.intrasoft.cbam.utils.Utils utils;

    EnvDataConfig envDataConfig;

    static WebDriverWait wait;

    WebDriver driver;

    public WaitUtils(com.intrasoft.cbam.utils.Utils utils) {
        this.utils = utils;
        envDataConfig = new EnvDataConfig();
        driver = com.intrasoft.cbam.utils.Utils.webDriverFactory().getDriver();
    }

    public void waitForLoad() {
        ExpectedCondition<Boolean> expectation =
                driver -> {
                    assert driver != null;
                    return ((JavascriptExecutor) utils.webDriverFactory().getDriver())
                            .executeScript("return document.readyState").toString().equals("complete");
                };
        try {
            utils.webDriverFactory().getWait().until(expectation);
            waitForLoadingImage();
            sleep(4500, TimeUnit.MILLISECONDS);
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for Page Load Request to complete.");
        }
    }

    public void forAlertToBePresent() {
        utils.webDriverFactory().getWait().until(ExpectedConditions.alertIsPresent());
    }

    public void waitForLoadingImage() {
        utils.webDriverFactory()
                .getFluentWait()
                .pollingEvery(Duration.ofMillis(250))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".spinner-box")));
        utils.webDriverFactory()
                .getFluentWait()
                .pollingEvery(Duration.ofMillis(250))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div [@class='spinner-container active']")));
    }

    public void waitForElement(WebElement element) {
        if (element != null) {
            utils.webDriverFactory().getWait()
                    .until(ExpectedConditions.visibilityOf(element));
        } else {
            System.out.println("ERROR Element is NOT present.");
        }
    }

    public void forElementToBePresent(By by) {
        try {
            com.intrasoft.cbam.utils.Utils.webDriverFactory().getWait().until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (Throwable error) {
            Assert.fail("Timeout waiting for element to be present.");
        }
    }

    public static void sleep(int timeout, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void waitForElementToBeClickable(By by) {
        try {
            utils.webDriverFactory().getFluentWait()
                    .until(ExpectedConditions.elementToBeClickable(by));
        } catch (Exception e) {
            System.out.println("ERROR Element is NOT present.");
        }
    }

    public void waitForDropdownElement(By by) {
        sleep(300, TimeUnit.MILLISECONDS);
        waitForElementToBeClickable(by);
    }

    public void waitForVisibilityOfElement(By by) {
        utils.webDriverFactory().getFluentWait()
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public Boolean isElementDisplayed(By by) {
        try {
            driver.findElement(by).isDisplayed();
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public void waitForInvisibilityOfElement(By element) {
        if (element != null) {
            utils.webDriverFactory().getWait().until(ExpectedConditions.invisibilityOfElementLocated(element));
        } else {
            System.out.println("ERROR Element is visible after " + envDataConfig.getTimeout() + " seconds");
        }

    }

    public void waitForPresenceOfElement(By by) {
        utils.webDriverFactory().getWait().until(ExpectedConditions.presenceOfElementLocated(by));
    }
}
