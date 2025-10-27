package config;

import org.testng.Assert;
import org.testng.util.Strings;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public final class TestDataConfig {
    private static Properties params;
    private static String propertyName;

    /**
     * This method returns the value of a String parameter from properties file
     *
     * @param testData      the target dataset [Properties file]
     * @param parameterName the name of the parameter as appears in the properties file [String]
     * @return the parameter value as String
     */
    public String getStringParameter(Properties testData, String parameterName) {
        if (!testData.containsKey(parameterName)) {
            Assert.fail("Parameter '" + parameterName + "' does not exist in Test Data!");
        }
        return testData.getProperty(parameterName);
    }

    /**
     * This method returns the value of a String parameter if it exists in the properties file, otherwise Null
     *
     * @param testData      the target dataset [Properties file]
     * @param parameterName the name of the parameter as appears in the properties file [String]
     * @return the parameter value as String
     */
    public String getStringParameterIfExists(Properties testData, String parameterName) {
        return parameterExists(testData, parameterName) ? testData.getProperty(parameterName) : null;
    }

    public static List<String> multipleOrSingleTestData(Properties params, String propertyName) {
        TestDataConfig.params = params;
        TestDataConfig.propertyName = propertyName;

        String PROPERTY_NAME = propertyName;
        String prmValue = params.getProperty(PROPERTY_NAME);
        String prmMulti = params.getProperty(PROPERTY_NAME + ".1");

        List<String> values = new ArrayList<>();

        if ((prmValue != null && !prmValue.isEmpty()) ||
                (prmMulti != null && !prmMulti.isEmpty())) {
            if (prmMulti != null && !prmMulti.isEmpty()) {
                Properties properties = getProperties(params, PROPERTY_NAME);
                if (properties.size() > 1) {
                    int i = 0;

                    while (properties.size() > i) {
                        int k = i + 1;
                        // Add each property in the list
                        values.add(params.getProperty(PROPERTY_NAME + "." + k));
                        i++;
                    }
                }
            } else {
                // Add only one property
                values.add(prmValue);
            }
        }
        return values;
    }

    /**
     * This method returns the value of a Boolean parameter from properties file
     *
     * @param testData      the target dataset [Properties file]
     * @param parameterName the name of the parameter as appears in the properties file [String]
     * @return the parameter value as Boolean
     */
    public boolean getBooleanParameter(Properties testData, String parameterName) {
        String value = getStringParameter(testData, parameterName);
        if (Strings.isNullOrEmpty(value)) {
            Assert.fail("Boolean parameter '" + parameterName + "' must have a value!");
        }
        //This is to cover cases in properties where "Yes" or "Checked" means true and "No" or "Unchecked" means false.
        if (value.equalsIgnoreCase("Yes") || value.equalsIgnoreCase("Checked")) {
            value = "true";
        } else if (value.equalsIgnoreCase("No") || value.equalsIgnoreCase("Unchecked")) {
            value = "false";
        }
        return Boolean.parseBoolean(value.toLowerCase());
    }

    /**
     * This method returns the value of an Integer parameter from properties file
     *
     * @param testData      the target dataset [Properties file]
     * @param parameterName the name of the parameter as appears in the properties file [String]
     * @return the parameter value as Integer
     */
    public int getIntegerParameter(Properties testData, String parameterName) {
        String value = getStringParameter(testData, parameterName);
        if (Strings.isNullOrEmpty(value)) {
            Assert.fail("Integer parameter '" + parameterName + "' must have a value!");
        }
        return Integer.parseInt(value);
    }

    /**
     * This method creates an array of Integer parameters from properties file
     *
     * @param testData        the target dataset [Properties file]
     * @param parameterPrefix the name of the parameter as appears in the properties file [String]
     * @return the parameter values as an array of Integers
     */
    public List<Integer> getMultipleIntegerParametersArray(Properties testData, String parameterPrefix) {
        return getMultipleIntegerParameters(testData, parameterPrefix);
    }

    /**
     * This method creates a List of Integer parameters from properties file
     *
     * @param testData        the target dataset [Properties file]
     * @param parameterPrefix the name of the parameter as appears in the properties file [String]
     * @return the parameter values as a List of Integers
     */
    public List<Integer> getMultipleIntegerParameters(Properties testData, String parameterPrefix) {
        List<Integer> values = new ArrayList<>();
        for (String value : getMultipleStringParameters(testData, parameterPrefix)) {
            values.add(Integer.parseInt(value));
        }
        return values;
    }

    /**
     * This method returns an array of String parameters if exist in the properties file, otherwise Null
     *
     * @param testData        the target dataset [Properties file]
     * @param parameterPrefix the name of the parameter as appears in the properties file [String]
     * @return the parameter values as an array of Strings or Null
     */
    public String[] getMultipleStringParametersIfExists(Properties testData, String parameterPrefix) {
        return parameterExists(testData, parameterPrefix) ?
                getMultipleStringParametersArray(testData, parameterPrefix) :
                null;
    }

    /**
     * This method creates an array of String parameters from properties file
     *
     * @param testData        the target dataset [Properties file]
     * @param parameterPrefix the name of the parameter as appears in the properties file [String]
     * @return the parameter values as an array of Strings
     */
    public String[] getMultipleStringParametersArray(Properties testData, String parameterPrefix) {
        return getMultipleStringParameters(testData, parameterPrefix).toArray(new String[0]);
    }

    /**
     * This method creates a List of String parameters from properties file
     *
     * @param testData        the target dataset [Properties file]
     * @param parameterPrefix the name of the parameter as appears in the properties file [String]
     * @return the parameter values as a List of Strings
     */
    public List<String> getMultipleStringParameters(Properties testData, String parameterPrefix) {
        Properties targetProperties = getParameters(testData, parameterPrefix);
        List<String> properties = new ArrayList<>();
        if (targetProperties.size() > 1) {
            for (int i = 1; i <= targetProperties.size(); i++) {
                String value = targetProperties.getProperty(String.valueOf(i));
                properties.add(value);
            }

        } else if (targetProperties.size() == 1) {
            properties = targetProperties.values().stream()
                    .map(value -> Objects.toString(value, null))
                    .collect(Collectors.toList());
        } else {
            Assert.fail("No parameters with prefix '" + parameterPrefix + "' found in Test Data!");
        }
        return properties;
    }

    /**
     * This method checks whether a given parameter exists in properties file.
     *
     * @param testData  the target dataset [Properties file]
     * @param parameter the name of the parameter as appears in the properties file [String]
     * @return true if parameter exists, false otherwise
     */
    public boolean parameterExists(Properties testData, String parameter) {
        Properties targetProperties = getParameters(testData, parameter);
        return targetProperties.size() > 0;
    }

    public Properties getTestData(String filepath, String dataSetPrefix) {
        Properties prop = loadTestData(filepath);
        if (dataSetPrefix != null) {
            return getProperties(prop, dataSetPrefix);
        } else {
            return prop;
        }
    }

    public String getTestUserData(String filepath, String userPropertyValue) {
        Properties prop = loadTestData(filepath);
        return prop.getProperty(userPropertyValue);
    }

    public Properties loadProperties(String testDataFile) {
        Properties prop = new Properties();
        try {
            InputStream inputStream = new FileInputStream(testDataFile);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            prop.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }

    private Properties loadTestData(String testDataFile) {
        return loadProperties(testDataFile);
    }

    @SuppressWarnings("rawtypes")
    public static Properties getProperties(Properties params, String prefix) {
        Properties result = new Properties();
        Enumeration names = params.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            if (name.indexOf(prefix) == 0) {
                result.put(name.substring(prefix.length() + 1), params.get(name));
            }
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public Properties getParameters(Properties params, String prefix) {
        Properties targetParameters = new Properties();

        Enumeration allParameters = params.propertyNames();
        while (allParameters.hasMoreElements()) {
            String parameter = (String) allParameters.nextElement();
            if (parameter.startsWith(prefix)) {
                if (parameter.length() == prefix.length()) {
                    targetParameters.put(parameter, params.get(parameter));
                } else if (parameter.substring(prefix.length()).startsWith(".")) {
                    targetParameters.put(parameter.substring(prefix.length() + 1), params.get(parameter));
                }
            }
        }
        return targetParameters;
    }
}
