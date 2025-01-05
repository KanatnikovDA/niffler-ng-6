package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Calendar;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

  public static String url = Config.getInstance().frontUrl() + "profile";

  private final SelenideElement avatar = $("#image__input").parent().$("img");
  private final SelenideElement userName = $("#username");
  private final SelenideElement nameInput = $("#name");
  private final SelenideElement photoInput = $("input[type='file']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement categoryInput = $("input[name='category']");
  private final SelenideElement archivedSwitcher = $(".MuiSwitch-input");
  private final ElementsCollection bubbles = $$(".MuiChip-filled.MuiChip-colorPrimary");
  private final ElementsCollection bubblesArchived = $$(".MuiChip-filled.MuiChip-colorDefault");

  private final Calendar calendar = new Calendar($(".ProfileCalendar"));

  @Step("Ввести name = {0}")
  @Nonnull
  public ProfilePage setName(String name) {
    nameInput.clear();
    nameInput.setValue(name);
    return this;
  }

  @Nonnull
  public ProfilePage uploadPhotoFromClasspath(String path) {
    photoInput.uploadFromClasspath(path);
    return this;
  }

  @Nonnull
  public ProfilePage addCategory(String category) {
    categoryInput.setValue(category).pressEnter();
    return this;
  }

  @Nonnull
  @Step("Проверить, наличие категории {0}")
  public ProfilePage checkCategoryExists(String category) {
    bubbles.find(text(category)).shouldBe(visible);
    return this;
  }

  @Nonnull
  @Step("Проверить, наличие архивной категории {0}")
  public ProfilePage checkArchivedCategoryExists(String category) {
    archivedSwitcher.click();
    bubblesArchived.find(text(category)).shouldBe(visible);
    return this;
  }

  @Nonnull
  public ProfilePage checkUsername(String username) {
    this.userName.should(value(username));
    return this;
  }

  @Nonnull
  @Step("Проверить, что Name содержит {0}")
  public ProfilePage checkName(String name) {
    nameInput.shouldHave(value(name));
    return this;
  }

  @Nonnull
  public ProfilePage checkPhotoExist() {
    avatar.should(attributeMatching("src", "data:image.*"));
    return this;
  }

  @Nonnull
  public ProfilePage checkThatCategoryInputDisabled() {
    categoryInput.should(disabled);
    return this;
  }

  @Nonnull
  public ProfilePage submitProfile() {
    submitButton.click();
    return this;
  }
}
