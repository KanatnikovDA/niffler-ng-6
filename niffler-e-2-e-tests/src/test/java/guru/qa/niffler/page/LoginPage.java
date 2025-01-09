package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<FriendsPage> {

  private final SelenideElement usernameInput = $("input[name='username']");
  private final SelenideElement passwordInput = $("input[name='password']");
  private final SelenideElement submitButton = $("button[type='submit']");
  private final SelenideElement registerButton = $("a[href='/register']");
  private final SelenideElement errorContainer = $(".form__error");

  @Nonnull
  public RegisterPage doRegister() {
    registerButton.click();
    return new RegisterPage();
  }

  @Nonnull
  @Step("Авторизоваться пользователем: {0}")
  public MainPage successLogin(String username, String password) {
    usernameInput.setValue(username);
    passwordInput.setValue(password);
    submitButton.click();
    return new MainPage();
  }

  @Nonnull
  @Step("Проверить что ошибка содержит текст: {0}")
  public LoginPage checkError(String error) {
    errorContainer.shouldHave(text(error));
    return this;
  }
}
