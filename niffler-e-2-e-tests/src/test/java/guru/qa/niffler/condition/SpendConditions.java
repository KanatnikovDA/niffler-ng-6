package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import com.github.jknack.handlebars.internal.lang3.ArrayUtils;
import guru.qa.niffler.model.rest.SpendJson;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;
import static java.lang.Double.valueOf;
import static java.lang.String.format;
import static java.util.Arrays.stream;

@ParametersAreNonnullByDefault
public class SpendConditions {

    @Nonnull
    public static WebElementsCondition spends(@Nonnull SpendJson... expectedSpends) {
        return new WebElementsCondition() {
            final List<SpendUi> expectedValues = stream(expectedSpends)
                    .map(spendJson -> new SpendUi(spendJson.category().name(),
                                                  spendJson.amount(),
                                                  spendJson.description(),
                                                  spendJson.spendDate()))
                    .toList();

            @Nonnull
            @Override
            public CheckResult check(Driver driver, List<WebElement> elements) {
                if (ArrayUtils.isEmpty(expectedSpends)) {
                    throw new IllegalArgumentException("No expected spends provided. The array cannot be empty.");
                }
                if (elements.size() != expectedSpends.length) {
                    String message = String.format("List size mismatch: expected %d, but found %d element(s).",
                                                   expectedSpends.length, elements.size());
                    throw new IllegalArgumentException(message);
                }

                final List<SpendUi> actualValue = elements
                        .stream()
                        .map(e -> {
                            List<WebElement> sells = e.findElements(By.cssSelector("td"));
                            SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy");
                            try {
                                return new SpendUi(sells.get(0).getText(),
                                            valueOf(sells.get(1).getText()),
                                            sells.get(2).getText(),
                                            formatter.parse(sells.get(3).getText()));
                            } catch (ParseException ex) {
                                throw new RuntimeException(ex);
                            }
                        })
                        .toList();

                if (!actualValue.containsAll(expectedValues)) {
                    final String actualValues = actualValue.toString();
                    final String message = format(
                            "List spends mismatch (expected: %s, actual: %s)", expectedValues, actualValues
                    );
                    return rejected(message, actualValues);
                }
                return accepted();
            }

            @Override
            public String toString() {
                return expectedValues.toString();
            }
        };
    }

    private record SpendUi(String category, Double amount, String description, Date spendDate) {
    }
}
