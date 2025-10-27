package config;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import org.testng.Assert;

public class RecoursesConfig {
    public String getOutputDir() {
        return getResourcesPath() + "/test-data/output-dir/";
    }

    public String getInputDir() {
        return getResourcesPath() + "/test-data/input-dir/";
    }

    public String getRequestReplyData() {
        return getResourcesPath() + "/test-data/input-dir/RequestReplyData/";
    }

    public String getStandaloneLitigation() {
        return getResourcesPath() + "/test-data/input-dir/standaloneLitigation/";
    }

    public String getSearchReportsData() {
        return getResourcesPath() + "/test-data/input-dir/searchReportsData/";
    }

    public String getRequestDelay() {
        return getResourcesPath() + "/test-data/input-dir/RequestDelaySubmission/";
    }

    public String getInitialData() {
        return getResourcesPath() + "/test-data/input-dir/InitialData/";
    }

    public String getO3CIData() {
        return getResourcesPath() + "/test-data/input-dir/CbamBridge/";
    }

    public String getMappingsInputDir() {
        return getResourcesPath() + "/test-data/input-dir/mappings/";
    }

    public String getNcaUploadFiles() {
        return getResourcesPath() + "/test-data/input-dir/ncaUploadFiles/";
    }

    public String getSurvDataCSVInitial() {
        return getResourcesPath() + "/test-data/input-dir/SurvDataInitial/csv/";
    }

    public String getSurvDataJSONInitial() {
        return getResourcesPath() + "/test-data/input-dir/SurvDataInitial/json/";
    }

    public String getNisInitialData() {
        return getResourcesPath() + "/test-data/input-dir/NisInitialData/";
    }

    public String getCbamBridge() { return getResourcesPath() + "/test-data/input-dir/CbamBridge/"; }

    public String getValidateReport() {
        return getResourcesPath() + "/test-data/input-dir/ValidateO3CIReports/";
    }

    public String getUploadZip() {
        return getResourcesPath() + "/test-data/input-dir/UploadZip/";
    }

    public String getTestDataDir() {
        return getResourcesPath() + "/test-data/";
    }

    public String getEnvironmentProperties() {
        if (System.getProperty("env.properties") == null) {
            return getAbsolutePath() + getPomProperty("env.properties");
        } else {
            return getAbsolutePath() + System.getProperty("env.properties");
        }
    }

    public String getUserProperties() {
        if (System.getProperty("env.properties") == null) {
            return getAbsolutePath() + getPomProperty("env.properties");
        } else {
            return getAbsolutePath() + System.getProperty("env.properties");
        }
    }

    public String getTanzuUserProperties() {
        if (System.getProperty("user_tanzu.properties") == null) {
            return getAbsolutePath() + getPomProperty("user_tanzu.properties");
        } else {
            return getAbsolutePath() + System.getProperty("user_tanzu.properties");
        }
    }

    public String getTestResourcesPath() {
        return getResourcesPath("test");
    }

    public String getResourcesPath() {
        return getResourcesPath("main");
    }

    private String getResourcesPath(String packageName) {
        String filePathString = getAbsolutePath() + "src/" + packageName + "/resources";
        File f = new File(filePathString);
        if (!f.exists())
            filePathString = getAbsolutePath();
        return filePathString;
    }

    public String getAbsolutePath() {
        String absPath = Paths.get(".")
                .toAbsolutePath().normalize().toString().replace("\\", "/");

        String modulePath = this.getClass().getClassLoader().getResource(".").getPath();
        modulePath = modulePath.replace("\\", "/");
        modulePath = modulePath.replace("/target/test-classes", "");
        modulePath = modulePath.replace("/target/classes", "");
        modulePath = modulePath.replace(absPath, "");
        modulePath = modulePath.replace("//", "/");

        return absPath + modulePath;
    }

    public String getTools() {
        String filePathString = getAbsolutePath() + "/tools";
        File f = new File(filePathString);
        if (!f.exists())
            filePathString = getAbsolutePath() + "/../../tools";
        return filePathString;
    }

    private String getPomProperty(String propertyName) {
        Model model = null;
        MavenXpp3Reader reader = new MavenXpp3Reader();
        try {
            model = reader.read(new FileReader(getAbsolutePath() + "/pom.xml"));
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return model.getProperties().getProperty(propertyName);
    }

    public String getTargetPath() {
        String absPath = Paths.get(".")
                .toAbsolutePath().normalize().toString().replace("\\", "/");

        String modulePath = this.getClass().getClassLoader().getResource(".").getPath();
        modulePath = modulePath.replace("\\", "/");
        modulePath = modulePath.replace("test-classes", "");
        modulePath = modulePath.replace("classes", "");
        modulePath = modulePath.replace(absPath, "");
        modulePath = modulePath.replace("//", "/");

        return absPath + modulePath;
    }


    public static String getAttachmentFilepath(String fileName) {
        File targetFile = new File(new ResourcesConfig().getUploadZip() + fileName);
        Assert.assertTrue(targetFile.exists() && targetFile.isFile(), "Could not locate attachment file: " + fileName);
        return targetFile.getAbsolutePath();
    }

    private static String bytesToBinary(byte[] bytes) {
        return new BigInteger(1, bytes).toString(2); // Convert bytes to binary string
    }

    private static String binaryToBase64(String binaryString) {
        byte[] bytes = new BigInteger(binaryString, 2).toByteArray(); // Convert binary string to bytes
        return Base64.getEncoder().encodeToString(bytes); // Encode bytes to Base64 string
    }

    public static String zipFileDecoder(String filename) {
        try {
            byte[] bytes = java.nio.file.Files.readAllBytes(Path.of(getAttachmentFilepath(filename)));
            String binaryString = bytesToBinary(bytes);
            String base64 = binaryToBase64(binaryString);
            return base64;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
