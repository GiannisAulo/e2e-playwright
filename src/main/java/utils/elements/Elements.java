package utils.elements;

import com.microsoft.playwright.Page;
import utils.elements.button.Button;
import utils.elements.checkbox.Checkbox;
import utils.elements.comboBox.ComboBox;
import utils.elements.dates.Datepicker;
import utils.elements.dropdown.Dropdown;
import utils.elements.input.Input;
import utils.elements.multiDropdown.MultiDropdown;
import utils.elements.radioButton.RadioButton;

public class Elements {
    private final Button button;
    private final Checkbox checkbox;
    private final ComboBox comboBox;
    private final Datepicker datepicker;
    private final Dropdown dropdown;
    private final Input input;
    private final MultiDropdown multiDropdown;
    private final RadioButton radioButton;
    private final Page page;

    public Elements(Page page) {
        this.button = new Button(page);
        this.checkbox = new Checkbox(page);
        this.comboBox = new ComboBox(page);
        this.datepicker = new Datepicker(page);
        this.dropdown = new Dropdown(page);
        this.input = new Input(page);
        this.multiDropdown = new MultiDropdown(page);
        this.radioButton = new RadioButton(page);
        this.page = page;
    }

    public Button button() {
        return button;
    }

    public Checkbox checkbox() {
        return checkbox;
    }

    public ComboBox comboBox() {
        return comboBox;
    }

    public Datepicker datepicker() {
        return datepicker;
    }

    public Dropdown dropdown() {
        return dropdown;
    }

    public Input input() {
        return input;
    }

    public MultiDropdown multiDropdown() {
        return multiDropdown;
    }

    public RadioButton radioButton() {
        return radioButton;
    }

    public boolean isElementPresent(String selector) {
        return page.locator(selector).count() > 0;
    }

    public boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public String getAttribute(String selector, String attributeName) {
        return page.locator(selector).getAttribute(attributeName);
    }

    public String getText(String selector) {
        return page.locator(selector).textContent().trim();
    }

    public void hover(String selector) {
        page.locator(selector).hover();
    }
}
