package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
public class SpendingWebTest {

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        final String newDescription = "Обучение Niffler Next Generation";

        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .getSpendingTable()
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingDescription(newDescription)
                .saveSpending();

        new MainPage().getSpendingTable()
                      .checkTableContains(newDescription);
    }

    @User
    @Test
    void shouldAddNewSpending(UserJson user) {
        String category = "Friends";
        int amount = 100;
        Date currentDate = new Date();
        String description = RandomDataUtils.randomSentence(3);

        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingCategory(category)
                .setNewSpendingAmount(amount)
                .setNewSpendingDate(currentDate)
                .setNewSpendingDescription(description)
                .saveSpending()
                .checkAlertMessage("New spending is successfully created");

        new MainPage().getSpendingTable()
                      .checkTableContains(description);
    }

    @User
    @Test
    void shouldNotAddSpendingWithEmptyCategory(UserJson user) {
        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingAmount(100)
                .setNewSpendingDate(new Date())
                .saveSpending()
                .checkFormErrorMessage("Please choose category");
    }

    @User
    @Test
    void shouldNotAddSpendingWithEmptyAmount(UserJson user) {
        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .getHeader()
                .addSpendingPage()
                .setNewSpendingCategory("Friends")
                .setNewSpendingDate(new Date())
                .saveSpending()
                .checkFormErrorMessage("Amount has to be not less then 0.01");
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @Test
    void deleteSpendingTest(UserJson user) {
        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .getSpendingTable()
                .deleteSpending("Обучение Advanced 2.0")
                .checkTableSize(0);
    }


    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/expected-stat.png", rewriteExpected = false)
    void checkStatComponentTest(UserJson user, BufferedImage expected) {
        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .checkStatisticDonutChart(expected);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/clearStat.png", rewriteExpected = false)
    void checkStatsAfterDeleteTest(UserJson user, BufferedImage expectedStatisticImage) {
        MainPage mainPage = open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage());

        mainPage.getSpendingTable()
                .deleteSpending("Обучение Advanced 2.0");

        mainPage.checkStatisticDonutChart(expectedStatisticImage)
                .checkStatisticCells(List.of("Обучение 79990 ₽"));
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest(value = "img/editStat.png", rewriteExpected = false)
    void checkStatsAfterEditTest(UserJson user, BufferedImage expectedStatisticImage) {
        MainPage mainPage = open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage());

        mainPage.getSpendingTable()
                .editSpending("Обучение Advanced 2.0")
                .setNewSpendingCategory("Edit test")
                .setNewSpendingAmount(1000)
                .saveSpending()
                .checkStatisticDonutChart(expectedStatisticImage)
                .checkStatisticCells(List.of("Edit test 1000 ₽"));
    }

    @User(
            categories = {
                    @Category(name = "Обучение"),
                    @Category(name = "Еда", archived = true)
            },
            spendings = { @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            ),
                          @Spending(
                                  category = "Еда",
                                  description = "Покупки на НГ",
                                  amount = 25000
                          )
            }
    )
    @ScreenShotTest(value = "img/archivedStat.png", rewriteExpected = false)
    void checkStatsWithArchivedTest(UserJson user, BufferedImage expectedStatisticImage) {
        open(LoginPage.URL, LoginPage.class)
                .fillLoginPage(user.username(), user.testData()
                                                    .password())
                .submit(new MainPage())
                .checkStatisticDonutChart(expectedStatisticImage)
                .checkStatisticCells(List.of("Обучение 79990 ₽", "Archived 25000 ₽"));
    }
}
