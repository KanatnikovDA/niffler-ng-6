package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.ClickOptions.usingJavaScript;
import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.textsInAnyOrder;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class FriendsPage {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");
    private final SelenideElement allTab = $("a[href='/people/all']");
    private final SelenideElement requestsTable = $("#requests");
    private final SelenideElement friendsTable = $("#friends");
    private final SelenideElement popup = $("[role='dialog']");

    @Step("Проверьте существующих друзей {0}")
    @Nonnull
    public FriendsPage checkExistingFriends(String... expectedUsernames) {
        friendsTable.$$("tr")
                    .shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Проверьте что друзья отсутствуют")
    @Nonnull
    public FriendsPage checkNoExistingFriends() {
        friendsTable.$$("tr")
                    .shouldHave(size(0));
        return this;
    }

    @Step("Проверить входящие приглашения")
    @Nonnull
    public FriendsPage checkExistingInvitations(String... expectedUsernames) {
        requestsTable.$$("tr")
                     .shouldHave(textsInAnyOrder(expectedUsernames));
        return this;
    }

    @Step("Проверить количество входящих приглашений {0}")
    @Nonnull
    public FriendsPage checkExistingInvitationsCount(int count) {
        requestsTable.$$("tr")
                     .shouldHave(size(count));
        return this;
    }

    @Step("Принять приглашения пользователя")
    @Nonnull
    public FriendsPage acceptInvite() {
        requestsTable.$$("tr")
                     .first()
                     .find(byText("Accept"))
                     .shouldBe(visible)
                     .click();
        return this;
    }

    @Step("Отменить приглашения пользователя")
    @Nonnull
    public FriendsPage declineInvite() {
        requestsTable.$$("tr")
                     .first()
                     .find(byText("Decline"))
                     .shouldBe(visible)
                     .click();
        popup.$(byText("Decline"))
             .click(usingJavaScript());
        return this;
    }
}
