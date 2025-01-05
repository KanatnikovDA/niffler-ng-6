package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

@WebTest
public class FriendsWebTest {

  private static final Config CFG = Config.getInstance();

  @Test
  void friendShouldBePresentInFriendsTable(@UserType(WITH_FRIEND) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.password())
        .checkThatPageLoaded()
        .friendsPage()
        .checkExistingFriends(user.friend());
  }

  @Test
  void friendsTableShouldBeEmptyForNewUser(@UserType(EMPTY) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.password())
        .checkThatPageLoaded()
        .friendsPage()
        .checkNoExistingFriends();
  }

  @Test
  void incomeInvitationBePresentInFriendsTable(@UserType(WITH_INCOME_REQUEST) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.password())
        .checkThatPageLoaded()
        .friendsPage()
        .checkExistingInvitations(user.income());
  }

  @Test
  void outcomeInvitationBePresentInAllPeoplesTable(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {
    open(CFG.frontUrl(), LoginPage.class)
        .successLogin(user.username(), user.password())
        .checkThatPageLoaded()
        .allPeoplesPage()
        .checkInvitationSentToUser(user.outcome());
  }

  @User(incomeInvitations = 1)
  @Test
  void acceptInvitation(UserJson user) {
    open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .header()
            .toFriendsPage()
            .checkExistingInvitationsCount(1)
            .acceptInvite()
            .checkExistingInvitationsCount(0);
  }

  @User(incomeInvitations = 1)
  @Test
  void declineInvitation(UserJson user) {
    open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .header()
            .toFriendsPage()
            .checkExistingInvitationsCount(1)
            .declineInvite()
            .checkExistingInvitationsCount(0);
  }
}
