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

    public Boolean getUUMDSMode() {
        return Boolean.parseBoolean(getEnvProperties().getProperty("uumds.mode"));
    }

    public static String getOutputDir() {
        String absPath = Paths.get(".").toAbsolutePath().normalize().toString();
        absPath = absPath.replace("\\", "/");
        return absPath + "/src/main/resources/test_data/outputDir/";
    }

    public Boolean getCCN2Mode() {
        return Boolean.parseBoolean(getEnvProperties().getProperty("ccn2.mode"));
    }

    public int getRetry() {
        return Integer.parseInt(getEnvProperties().getProperty("retry"));
    }

    public String getEUAUTHURL(Actors actor) {
        if (getUserPortal(actor.toString()).contains("euauth-p1"))
            return removeTrailingSlash(getEnvProperties().getProperty("euauth-p1.url"));
        else
            return removeTrailingSlash(getEnvProperties().getProperty("euauth-p2.url"));
    }

    public String getTraderURL() {
        return removeTrailingSlash(getEnvProperties().getProperty("tp.url"));
    }

    public String getAPITPURL() {
        return removeTrailingSlash(getEnvProperties().getProperty("api-tp.url"));
    }

    public String getPreferredBridgeUrl() {
        return removeTrailingSlash(getEnvProperties().getProperty("preferred.bridge.url"));
    }

    public String getRdAuthorizationToken() {
        return removeTrailingSlash(getEnvProperties().getProperty("x_AuthorizationRdTokenBridge"));
    }

    public String getO3CiAuthorizationToken() {
        return removeTrailingSlash(getEnvProperties().getProperty("x_AuthorizationO3CiTokenBridge"));
    }

    public String getCloseBrowsersProperty() {
        return removeTrailingSlash(getEnvProperties().getProperty("closeBrowsers"));
    }

    public String getCleanUpAfterSuiteProperty() {
        return removeTrailingSlash(getEnvProperties().getProperty("cleanUpAfterSuite"));
    }

    public String getLoadDataAfterSuiteValue() {
        return removeTrailingSlash(getEnvProperties().getProperty("loadDataAfterSuite"));
    }

    public String disableReferenceDataCaching() {
        return removeTrailingSlash(getEnvProperties().getProperty("disableRefDataCaching"));
    }

    public String getAPIEUAUTHURL(Actors actor) {
        if (getUserPortal(actor.toString()).contains("euauth-p1"))
            return removeTrailingSlash(getEnvProperties().getProperty("api-euauthP1.url"));
        else
            return removeTrailingSlash(getEnvProperties().getProperty("api-euauthP2.url"));
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

    public String getFisrtName(String userRole) {
        return getEnvProperties().getProperty(userRole + ".firstName");
    }

    public String getLastName(String userRole) {
        return getEnvProperties().getProperty(userRole + ".lastName");
    }

    public String getRoles(String userRole) {
        return getEnvProperties().getProperty(userRole + ".roles");
    }

    public String getEORI(String userRole) {
        return getEnvProperties().getProperty(userRole + ".eori");
    }

    public String getImporterIdentifier(String userRole) {
        return getEnvProperties().getProperty(userRole + ".importerId");
    }

    public String getUnit(String userRole) {
        return getEnvProperties().getProperty(userRole + ".unit");
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

    public String getAddress(String userRole) {
        return getEnvProperties().getProperty(userRole + ".address");
    }

    public String getCity(String userRole) {
        return getEnvProperties().getProperty(userRole + ".city");
    }

    //P2 integration
    public String getTokenRd() {
        return removeTrailingSlash(getEnvProperties().getProperty("tokenRd.url"));
    }

    public String getAWSTPEnvUrl() {
        return removeTrailingSlash(getEnvProperties().getProperty("api-tpAWS.url"));
    }

    public String getAWSRDEnvUrl() {
        return removeTrailingSlash(getEnvProperties().getProperty("appRd.url"));
    }

    public String getAWSO3ciEnvUrl() {
        return removeTrailingSlash(getEnvProperties().getProperty("appO3ci.url"));
    }

    public String getCOMURL() {
        return removeTrailingSlash(getEnvProperties().getProperty("comO3ci.url"));
    }

    public String getNCAURL() {
        return removeTrailingSlash(getEnvProperties().getProperty("ncaO3ci.url"));
    }

    public Boolean getTanzuMode() {
        return Boolean.parseBoolean(getEnvProperties().getProperty("tanzu.mode"));
    }

    public String getDECLURL() {
        return removeTrailingSlash(getEnvProperties().getProperty("declO3ci.url"));
    }

    public String getTokenO3ci() {
        return removeTrailingSlash(getEnvProperties().getProperty("tokenO3ci.url"));
    }

    public String getSchemaDB() {
        return getEnvProperties().getProperty("schema.db");
    }

    public String getRegistrySchemaDB() {
        return getEnvProperties().getProperty("schema.registry.db");
    }

    public String getDbManagementUrl() {
        return getEnvProperties().getProperty("db.management.url");
    }

    public String getDbRegistryUrl() {
        return getEnvProperties().getProperty("db.registry.url");
    }

    public String getIntDbPassword() {
        return getEnvProperties().getProperty("dbInt.password");
    }

    public String getO3ciDbUrl() {
        return getEnvProperties().getProperty("db.o3ci.url");
    }
    public String getComDbUrl() {
        return getEnvProperties().getProperty("db.com.url");
    }

    public String getNcaDbUrl() {
        return getEnvProperties().getProperty("db.nca.url");
    }

    public String getRegDbUrl() {
        return getEnvProperties().getProperty("dbO3ci.registry.url");
    }

    public String getUsername(Boolean tanzuMode, String user) {
        if (tanzuMode)
            return getTanzuUserProperties().getProperty(user + ".username");
        else
            return getUserProperties().getProperty(user + ".username");
    }

    public String getPassword(Boolean tanzuMode,String user) {
        if (tanzuMode)
            return getTanzuUserProperties().getProperty(user + ".password");
        else
            return getUserProperties().getProperty(user + ".password");
    }

    public String getOperatorId(Boolean tanzuMode,String user) {
        if (tanzuMode)
            return getTanzuUserProperties().getProperty(user + ".operatorId");
        else
            return getUserProperties().getProperty(user + ".operatorId");
    }

    public String getOperatorName(Boolean tanzuMode,String user) {
        if (tanzuMode)
            return getTanzuUserProperties().getProperty(user + ".operatorName");
        else
            return getUserProperties().getProperty(user + ".operatorName");
    }

    public String getCountryCode(Boolean tanzuMode,String user) {
        if (tanzuMode)
            return getTanzuUserProperties().getProperty(user + ".countryCode");
        else
            return getUserProperties().getProperty(user + ".countryCode");
    }

    public String getStaticOperToken(Boolean tanzuMode, String user) {
        if (tanzuMode)
            return getTanzuUserProperties().getProperty(user + ".token");
        else
            return getUserProperties().getProperty(user + ".token");
    }

    private Properties getUserProperties() {
        return getProperties(loadProperties(resourcesConfig.getUserProperties()));
    }

    private Properties getTanzuUserProperties() {
        return getProperties(loadProperties(resourcesConfig.getTanzuUserProperties()));
    }

    public String getSchemaOperatorDB() { return getEnvProperties().getProperty("schema.operator.db"); }
    public String getSchemaO3ciDMSDB() { return getEnvProperties().getProperty("schema.o3ciDMS.db"); }
    public String getSchemaCOMDB() { return getEnvProperties().getProperty("schema.com.db"); }

    public String getSchemaComDMSDB() {
        return getEnvProperties().getProperty("schema.comDMS.db");
    }
    public String getSchemaRegBackDB() { return getEnvProperties().getProperty("schema.registryBackend.db");}
    public String getSchemaRegDMSDB() { return getEnvProperties().getProperty("schema.registryDMS.db");}
    public String getSchemaNCABackDB() { return getEnvProperties().getProperty("schema.nca.db"); }
    public String getSchemaNcaDMSDB() { return getEnvProperties().getProperty("schema.ncaDMS.db"); }


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

    private Properties getEnvProperties() {
        return getProperties(loadProperties(resourcesConfig.getEnvironmentProperties()));
    }
}
