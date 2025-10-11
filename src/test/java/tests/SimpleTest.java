package tests;

import org.testng.annotations.Test;

public class SimpleTest extends BaseTest{
    @Test
    public void openExamplePage() {
        page.navigate("http://localhost:63342/e2e-playwright/test-page.html?_ijt=spaf9ge8h1nie6tv01g4ctko3g&_ij_reload=RELOAD_ON_SAVE");
        System.out.println("Title: " + page.title());
    }
}
