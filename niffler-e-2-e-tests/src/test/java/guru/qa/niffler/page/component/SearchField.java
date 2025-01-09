package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class SearchField extends BaseComponent<SearchField> {
    private final SelenideElement searchField = self.find("input[type='text']");

    public SearchField() {
        super($("input[aria-label='search']"));
    }

    @Step("Поиск по значению: {value}")
    public SearchField search(String value) {
        searchField.setValue(value).pressEnter();
        return this;
    }

    @Step("Очистить строку поиска")
    public SearchField clearIfNotEmpty() {
        searchField.clear();
        return this;
    }
}
