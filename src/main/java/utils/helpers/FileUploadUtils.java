package utils.helpers;

import com.microsoft.playwright.Page;
import org.testng.Assert;
import utils.elements.Elements;

public class FileUploadUtils {
    private final Elements elements;
    private final Page page;

    public FileUploadUtils(Page page) {
        this.page = page;
        elements = new Elements(page);
    }

    public void verifyFileUploaded() {
        Assert.assertTrue(elements.isElementPresent("div.file-upload__preview__filename"));
        Assert.assertTrue(elements.isElementPresent("div.file-upload__preview__filesize"));
        Assert.assertTrue(elements.isElementPresent("div.file-upload__preview__filetype"));
        Assert.assertTrue(elements.isElementPresent("div.file-upload__total-size"));
    }

    public void verifyFileUploadedIsRemoved() {
        Assert.assertFalse(elements.isElementPresent("div.file-upload__preview__filename"));
        Assert.assertFalse(elements.isElementPresent("div.file-upload__preview__filesize"));
        Assert.assertFalse(elements.isElementPresent("div.file-upload__preview__filetype"));
        Assert.assertFalse(elements.isElementPresent("div.file-upload__total-size"));
    }


}