package guru.qa.niffler.page.component;


import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class SpendingTable extends BaseComponent<SpendingTable> {
    private final SelenideElement spends = self.find(".MuiTableContainer-root");
    private final SelenideElement deleteButton = self.find("#delete");

    private final ElementsCollection timePeriods = self.findAll("[role='option']");

    private static final String deleteConfirmButton = ".MuiDialogActions-spacing [type='button']:nth-child(2)";
    private static final String spendingRow = "tbody tr";
    private static final String spendingColumn = "td:nth-child(4)";

    public SpendingTable() {
        super($("#spendings"));
    }

    @Nonnull
    @Step("Выбор периода для отображения трат: {period}")
    public SpendingTable selectPeriod(@Nonnull String period) {
        spends.$("#period").click();
        timePeriods.find(text(period)).click();
        return this;
    }

    @Nonnull
    @Step("Изменения описания траты на: {spendingDescription}")
    public EditSpendingPage editSpending(@Nonnull String description) {
        spends.$$(spendingRow).find(text(description)).$(" [aria-label='Edit spending']").click();
        return new EditSpendingPage();
    }

    @Nonnull
    @Step("Удаление траты с описанием: {description}")
    public SpendingTable deleteSpending(@Nonnull String description) {
        spends.$(spendingRow).$$("tr").find(text(description)).$$("td").get(1).click();
        deleteButton.shouldBe(visible).click();
        $(deleteConfirmButton).shouldBe(visible).click();
        return this;
    }

    @Nonnull
    @Step("Поиск траты с описанием: {description}")
    public SpendingTable searchSpendingByDescription(@Nonnull String description) {
        spends.$(spendingRow).$$("tr").find(text(description)).shouldBe(visible);
        return this;
    }

    @Nonnull
    @Step("Проверка, что таблица содержит траты: {expectedSpends}")
    public SpendingTable checkTableContains(@Nonnull String... expectedSpends) {
        spends.$(spendingRow).$("td").$$(spendingColumn).shouldHave(textsInAnyOrder(expectedSpends));
        return this;
    }

    @Nonnull
    @Step("Проверка, что количество трат равно: {expectedSize}")
    public SpendingTable checkTableSize(int expectedSize) {
        spends.$(spendingRow).$$("tr").shouldHave(size(expectedSize));
        return this;
    }
}

