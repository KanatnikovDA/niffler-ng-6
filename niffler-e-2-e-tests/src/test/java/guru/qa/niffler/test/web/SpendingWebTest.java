package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();


    @User(
            username = "duck",
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(SpendJson spend) {
        final String newDescription = "Обучение Niffler Next Generation";
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin("duck", "12345")
                .editSpending(spend.description())
                .setNewSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void addNewSpending(UserJson user) {
        open(CFG.frontUrl(), LoginPage.class)
                .successLogin(user.username(), user.testData().password())
                .header()
                .addSpendingPage()
                .addAmount("300")
                .addCategoryName("categoryName")
                .addDate(new Date())
                .addDescription("description")
                .save();
    }
}

