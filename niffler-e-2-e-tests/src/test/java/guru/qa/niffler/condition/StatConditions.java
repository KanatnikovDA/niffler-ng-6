package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementCondition;
import com.codeborne.selenide.WebElementsCondition;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import static java.lang.String.format;
import static java.util.Arrays.stream;

@ParametersAreNonnullByDefault
public class StatConditions {

    static final String bubblesValue = "%s %s";

    @Nonnull
    public static WebElementCondition color(Color expectedColor) {
        return new WebElementCondition("color " + expectedColor.rgb) {
            @NotNull
            @Override
            public CheckResult check(Driver driver, WebElement element) {
                final String rgba = element.getCssValue("background-color");
                return new CheckResult(
                        expectedColor.rgb.equals(rgba),
                        rgba
                );
            }
        };
    }

    @Nonnull
    public static WebElementsCondition color(@Nonnull Color... expectedColors) {
        return new WebElementsCondition() {

            private final String expectedRgba = Arrays.stream(expectedColors)
                                                      .map(c -> c.rgb)
                                                      .toList()
                                                      .toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedColors)) {
                    throw new IllegalArgumentException("No expected colors given");
                }
                if (expectedColors.length != elements.size()) {
                    final String message = String.format("List size mismatch (expected: %s, actual: %s)", expectedColors.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                final List<String> actualRgbaList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final WebElement elementToCheck = elements.get(i);
                    final Color colorToCheck = expectedColors[i];
                    final String rgba = elementToCheck.getCssValue("background-color");
                    actualRgbaList.add(rgba);
                    if (passed) {
                        passed = colorToCheck.rgb.equals(rgba);
                    }
                }

                if (!passed) {
                    final String actualRgba = actualRgbaList.toString();
                    final String message = format(
                            "List colors mismatch (expected: %s, actual: %s)", expectedRgba, actualRgba
                    );
                    return rejected(message, actualRgba);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedRgba;
            }
        };
    }

    public static WebElementsCondition statBubbles(@NotNull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final String expectedValues = stream(expectedBubbles)
                    .map(c -> format(bubblesValue, c.color().rgb, c.text()))
                    .toList()
                    .toString();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected expectedBubbles given");
                }
                if (expectedBubbles.length != elements.size()) {
                    final String message = format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                boolean passed = true;
                List<String> actualBubbleList = new ArrayList<>();
                for (int i = 0; i < elements.size(); i++) {
                    final Bubble bubbleCheck = expectedBubbles[i];
                    final String rgba = elements.get(i).getCssValue("background-color");
                    final String text = elements.get(i).getText();

                    actualBubbleList.add(format(bubblesValue, rgba, text));
                    if (passed) {
                        passed = bubbleCheck.color().rgb.equals(rgba) &&
                                 bubbleCheck.text().equals(text);
                    }
                }

                if (!passed) {
                    final String actualBubble = actualBubbleList.toString();
                    final String message = format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedValues, actualBubble
                    );
                    return rejected(message, actualBubble);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedValues;
            }
        };
    }

    public static WebElementsCondition statBubblesInAnyOrder(@NotNull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final List<String> expectedValuesList = stream(expectedBubbles)
                    .map(c -> format(bubblesValue, c.color().rgb, c.text()))
                    .toList();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected expectedBubbles given");
                }

                if (expectedBubbles.length != elements.size()) {
                    final String message = format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                final List<String> actualValuesList = elements.stream()
                                                              .map(e -> e.getCssValue("background-color") + " " + e.getText())
                                                              .toList();

                if (!actualValuesList.containsAll(expectedValuesList)) {
                    final String actualValues = actualValuesList.toString();
                    final String message = format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedValuesList, actualValues
                    );
                    return rejected(message, actualValues);
                }

                return accepted();
            }

            @Override
            public String toString() {
                return expectedValuesList.toString();
            }
        };
    }

    public static WebElementsCondition statBubblesContains(@NotNull Bubble... expectedBubbles) {
        return new WebElementsCondition() {
            private final List<String> expectedValuesList = stream(expectedBubbles)
                    .map(c -> format(bubblesValue, c.color().rgb, c.text()))
                    .toList();

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedBubbles)) {
                    throw new IllegalArgumentException("No expected expectedBubbles given");
                }

                if (expectedBubbles.length > elements.size()) {
                    final String message = format("List size mismatch (expected: %s, actual: %s)", expectedBubbles.length, elements.size());
                    return rejected(message, elements);
                }

                final List<String> actualValuesList = elements.stream()
                                                              .map(e -> format(bubblesValue, e.getCssValue("background-color"), e.getText()))
                                                              .toList();

                if (!actualValuesList.containsAll(expectedValuesList)) {
                    final String actualValues = actualValuesList.toString();
                    final String message = format(
                            "List bubbles mismatch (expected: %s, actual: %s)", expectedValuesList, actualValues
                    );
                    return rejected(message, actualValues);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedValuesList.toString();
            }
        };
    }
}
