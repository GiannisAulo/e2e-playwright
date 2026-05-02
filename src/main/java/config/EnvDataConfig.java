package config;

import utils.enums.Actors;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.Properties;

public class EnvDataConfig {
    RecoursesConfig resourcesConfig;

    public EnvDataConfig() {
        resourcesConfig = new RecoursesConfig();
    }

    public int getDownloadTimeOut() {
        return Integer.parseInt(getEnvProperties().getProperty("app.timeOutInSec"));
    }

    public int getPageLoadSleep() {
        return Integer.parseInt(getEnvProperties().getProperty("app.sleepInMillis"));
    }

    public String getTimeout() {
        return getEnvProperties().getProperty("timeout");
    }

    public String getInterval() {
        return getEnvProperties().getProperty("interval");
    }

    public String getBrowser() {
        return getEnvProperties().getProperty("browser");
    }

    public String getRemoteURL() {
        return removeTrailingSlash(getEnvProperties().getProperty("driver.remote.url"));
    }

    public Boolean getHeadlessMode() {
        return Boolean.parseBoolean(getEnvProperties().getProperty("headless.mode"));
    }

    public static String getOutputDir() {
        String absPath = Paths.get(".").toAbsolutePath().normalize().toString();
        absPath = absPath.replace("\\", "/");
        return absPath + "/src/main/resources/test_data/outputDir/";
    }

    public int getRetry() {
        return Integer.parseInt(getEnvProperties().getProperty("retry"));
    }

    public boolean getFallbackLink() {
        return Boolean.parseBoolean(getEnvProperties().getProperty("fallbackLink"));
    }

    public String getUserPortal(String userRole) {
        return getEnvProperties().getProperty(userRole + ".portal");
    }

    public String getUsername(String userRole) {
        return getEnvProperties().getProperty(userRole + ".username");
    }

    public String getPassword(String userRole) {
        return getEnvProperties().getProperty(userRole + ".password");
    }

    public String getCountry(String userRole) {
        return getEnvProperties().getProperty(userRole + ".country");
    }

    public String getSubdomain(String userRole) {
        return getEnvProperties().getProperty(userRole + ".subdomain");
    }

    public String getTypeOfActor(String userRole) {
        return getEnvProperties().getProperty(userRole + ".typeOfActor");
    }

    public String getTypeOfPerson(String userRole) {
        return getEnvProperties().getProperty(userRole + ".typeOfPerson");
    }

    public String getEmail(String userRole) {
        return getEnvProperties().getProperty(userRole + ".email");
    }

    public String getCrsEmail(String userRole) {
        return getEnvProperties().getProperty(userRole + ".crsEmail");
    }

    public String getUumdsEmail(String userRole) {
        return getEnvProperties().getProperty(userRole + ".uumdsEmail");
    }
    
    public String getLastName(String userRole) {
        return getEnvProperties().getProperty(userRole + ".lastName");
    }

    public String getRoles(String userRole) {
        return getEnvProperties().getProperty(userRole + ".roles");
    }

    public String getDbUrl() {
        return getEnvProperties().getProperty("db.url");
    }

    public String getDbUsername() {
        return getEnvProperties().getProperty("db.username");
    }

    public String getDbPassword() {
        return getEnvProperties().getProperty("db.password");
    }

    private Properties getUserProperties() {
        return getProperties(loadProperties(resourcesConfig.getUserProperties()));
    }

    private Properties getTanzuUserProperties() {
        return getProperties(loadProperties(resourcesConfig.getTanzuUserProperties()));
    }


    private static Properties loadProperties(String testDataFile) {
        Properties prop = new Properties();
        try {
            InputStream inputStream = new FileInputStream(testDataFile);
            Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            prop.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    private static Properties getProperties(Properties params) {
        Properties result = new Properties();
        Enumeration<?> names = params.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            result.put(name, params.get(name));
        }
        return result;
    }

    private static String removeTrailingSlash(String url) {
        for (int i = 0; i < url.length(); i++) {
            if (url.endsWith("/")) {
                url = StringUtils.removeEnd(url, "/");
            } else {
                break;
            }
        }
        return url;
    }

    public Properties getEnvProperties() {
        return getProperties(loadProperties(resourcesConfig.getEnvironmentProperties()));
    }
}
