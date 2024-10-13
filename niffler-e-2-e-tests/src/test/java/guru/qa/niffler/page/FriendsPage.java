package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class FriendsPage {
    private final SelenideElement emptyFriends = $x("//p[text()='There are no users yet']");
    private final SelenideElement myFriendsListHeader = $x("//h2[text()='My friends']");
    private final SelenideElement friendsRequestListHeader = $x("//h2[text()='Friend requests']");
    private final ElementsCollection friendsRows = $$x("//tbody[@id='friends']/tr");
    private final ElementsCollection requestsRows = $$x("//tbody[@id='requests']/tr");
    private final ElementsCollection allPeopleRows = $$("tbody#all tr");

    public void checkFriendPresentInFriendsTable(String friendName) {
        friendsRows.findBy(text(friendName)).shouldBe(visible);
    }

    public void checkFriendsTableIsEmpty(String message) {
        emptyFriends.shouldHave(text(message)).shouldBe(visible);
    }

    public FriendsPage checkFriendPresentInAllPeopleTable(String name) {
        allPeopleRows.find(text(name)).shouldBe(visible);
        return this;
    }
}

