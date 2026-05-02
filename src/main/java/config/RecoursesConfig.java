package config;

import java.io.File;
import java.nio.file.Paths;

public class RecoursesConfig {
    public String getOutputDir() {
        return getResourcesPath() + "/test-data/output-dir/";
    }

    public String getInputDir() {
        return getResourcesPath() + "/test-data/input-dir/";
    }

    public String getInitialData() {
        return getResourcesPath() + "/test-data/input-dir/InitialData/";
    }

    public String getTestDataDir() {
        return getResourcesPath() + "/test-data/";
    }

    public String getEnvironmentProperties() {
        if (System.getProperty("env.properties") != null) {
            return getAbsolutePath() + System.getProperty("env.properties");
        }
        return getAbsolutePath() + "env.properties";
    }

    public String getUserProperties() {
        if (System.getProperty("env.properties") != null) {
            return getAbsolutePath() + System.getProperty("env.properties");
        }
        return getAbsolutePath() + "env.properties";
    }

    public String getTanzuUserProperties() {
        if (System.getProperty("user_tanzu.properties") != null) {
            return getAbsolutePath() + System.getProperty("user_tanzu.properties");
        }
        return getAbsolutePath() + "user_tanzu.properties";
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
}
