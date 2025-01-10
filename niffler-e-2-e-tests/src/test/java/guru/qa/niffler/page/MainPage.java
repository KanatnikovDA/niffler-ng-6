package guru.qa.niffler.page;

import com.codeborne.selenide.*;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainPage extends BasePage<MainPage> {

  public static final String URL = CFG.frontUrl() + "main";
  private final SelenideElement statisticCanvas = $("canvas[role='img']");

  protected final Header header = new Header();
  protected final SpendingTable spendingTable = new SpendingTable();
  protected final StatComponent statComponent = new StatComponent();

  private final ElementsCollection statisticCells = $$("#legend-container li");

  @Nonnull
  public Header getHeader() {
    return header;
  }

  @Nonnull
  public SpendingTable getSpendingTable() {
    spendingTable.getSelf().scrollIntoView(true);
    return spendingTable;
  }

  @Step("Check that page is loaded")
  @Override
  @Nonnull
  public MainPage checkThatPageLoaded() {
    header.getSelf().should(visible).shouldHave(text("Niffler"));
    statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
    spendingTable.getSelf().should(visible).shouldHave(text("History of Spendings"));
    return this;
  }

  @SneakyThrows
  @Nonnull
  @Step("Check donut chart")
  public MainPage checkStatisticDonutChart(@Nonnull BufferedImage expected) {
    Selenide.sleep(3000); //Wait until Donut chart will be loaded
    BufferedImage actualImage = ImageIO.read(requireNonNull(statisticCanvas.screenshot()));
    assertFalse(new ScreenDiffResult(actualImage, expected));
    return this;
  }

  @Step("Check that statistic cells contain texts {texts}")
  @Nonnull
  public MainPage checkStatisticCells(List<String> texts) {
    statisticCells.shouldHave(CollectionCondition.sizeGreaterThanOrEqual(1))
                  .shouldHave(CollectionCondition.exactTexts(texts));
    return this;
  }
}
