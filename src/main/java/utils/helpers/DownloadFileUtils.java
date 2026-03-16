package utils.helpers;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Page;
import org.testng.Assert;
import utils.waits.WaitUtils;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DownloadFileUtils {
    private final Page page;
    private final String FILENAME = "fileName";

    public DownloadFileUtils(Page page) {
        this.page = page;
    }

    /**
     * Verifies the downloaded filename
     * verifyFileIsDownloaded(params, () -> page.click("#downloadButton"));
     *
     * @param params
     * @param downloadAction
     */
    public void verifyFileIsDownloaded(Properties params, Runnable downloadAction) {
        String expectedFilename = params.getProperty(FILENAME);

        Download download = page.waitForDownload(downloadAction);

        String actualFilename = download.suggestedFilename();
        Assert.assertTrue(actualFilename.contains(expectedFilename),
                "The file is not downloaded. Expected: " + expectedFilename + " but found: " + actualFilename);
    }

    public boolean verifyDownloadedFileExists(String fileName) {
        WaitUtils.sleep(5, TimeUnit.SECONDS);
        String userProfile = System.getenv("USERPROFILE");
        String path = userProfile + "\\Downloads";
        File dir = new File(path);
        File[] dirContents = dir.listFiles();
        boolean found = false;
        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(fileName)) {
                System.out.println("File Found !!!");
                dirContents[i].delete();
                found = true;
                return found;
            }
        }
        Assert.assertEquals(found, false, "File not found");
        return false;
    }
}
