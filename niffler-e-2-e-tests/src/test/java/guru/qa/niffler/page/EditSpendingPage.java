package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class EditSpendingPage extends BasePage<EditSpendingPage> {

    private final SelenideElement descriptionInput = $("#description");
    private final SelenideElement saveBtn = $("#save");
    private final Calendar calendar = new Calendar($(".MuiPickersLayout-root"));
    private final SelenideElement calendarPick = $("button[aria-label='Choose date']");
    private final SelenideElement amountEl = $("#amount");
    private final SelenideElement catNameEl = $("#category");
    private final SelenideElement descriptionEl = $("#desscription");

    @Nonnull
    @Step("Установить новое описание")
    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Nonnull
    @Step("Ввести сумму")
    public EditSpendingPage addAmount(String amount) {
        amountEl.setValue(amount);
        return this;
    }

    @Nonnull
    @Step("Ввести имя категории")
    public EditSpendingPage addCategoryName(String categoryName) {
        catNameEl.setValue(categoryName);
        return this;
    }

    @Nonnull
    @Step("Ввести дату")
    public EditSpendingPage addDate(Date date) {
        calendarPick.click();
        calendar.selectDateInCalendar(date);
        return this;
    }

    @Nonnull
    @Step("Ввести описание")
    public EditSpendingPage addDescription(String desc) {
        descriptionEl.setValue(desc);
        return this;
    }

    @Step("Нажать сохранить")
    public EditSpendingPage save() {
        saveBtn.click();
        return this;
    }
}
