package common;

public class RecoursesConfig {
    public static String getTestDataFile(String fileName) {
        return RecoursesConfig.getAbsolutePath() + "/testData" + fileName;
    }

    public static String getAbsolutePath() {
        return "C:/Users/user/IdeaProjects/e2e-playwright/src/main/resources";
    }
}
