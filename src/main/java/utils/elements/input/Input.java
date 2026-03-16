package utils.elements.input;

import com.microsoft.playwright.Page;

public class Input {
    private final Page page;

    public Input(Page page) {
        this.page = page;
    }

    public String getText(String selector){
        return  page.locator(selector).textContent().trim();
    }
}
