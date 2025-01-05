package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar extends BaseComponent<Calendar> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public Calendar(SelenideElement calendarInput) {
        super(calendarInput);

    }

    @Step("Выбор даты в календаре: {date}")
    public void selectDateInCalendar(Date date) {
        String formattedDate = dateFormat.format(date);
        self.setValue(formattedDate)
            .pressEnter();
    }
}
