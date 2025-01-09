package guru.qa.niffler.test.web;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.open;

@WebTest
public class ProfileTest {

  private static final Config CFG = Config.getInstance();

  @User(
      username = "duck",
      categories = @Category(
          archived = true
      )
  )
  @Test
  void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
    open(CFG.frontUrl(), LoginPage.class)
        .successLogin("duck", "12345")
        .checkThatPageLoaded();

    open(CFG.frontUrl() + "profile", ProfilePage.class)
        .checkArchivedCategoryExists(category.name())
        .checkName("")
        .checkAlert("")
        .checkName("");
  }

  @User(
      username = "duck",
      categories = @Category()
  )
  @Test
  void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
    open(CFG.frontUrl(), LoginPage.class)
        .successLogin("duck", "12345")
        .checkThatPageLoaded();

    open(CFG.frontUrl() + "profile", ProfilePage.class)
        .checkCategoryExists(category.name());
  }

  @User
  @Test
  void userShouldEditProfile(UserJson user) {
    open(CFG.frontUrl(), LoginPage.class)
            .successLogin(user.username(), user.testData().password())
            .header()
            .toProfilePage()
            .setName("Name")
            .checkAlert("Profile successfully updated")
            .checkName("Name");
  }
}
