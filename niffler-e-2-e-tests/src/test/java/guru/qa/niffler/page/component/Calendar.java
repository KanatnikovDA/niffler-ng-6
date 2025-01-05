package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;

public class Calendar {
    private final SelenideElement calendarInput;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public Calendar(SelenideElement calendarInput) {
        this.calendarInput = calendarInput;
    }

    @Step("Выбор даты в календаре: {date}")
    public void selectDateInCalendar(Date date) {
        String formattedDate = dateFormat.format(date);
        calendarInput.setValue(formattedDate).pressEnter();
    }
}
