package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.Files.newInputStream;

public class AllureBackendLogsExtension implements SuiteExtension {

    public static final String caseName = "Niffler backend logs";


    @Override
    public void afterSuite() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID()
                                  .toString();
        allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId)
                                                         .setName(caseName));
        allureLifecycle.startTestCase(caseId);

        addAllServicesLogAttachment();

        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);
    }

    @SneakyThrows
    private void addAllServicesLogAttachment() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String type = "text/html";
        final String fileExtension = ".log";

        allureLifecycle.addAttachment("Niffler auth log", type, fileExtension, newInputStream(Path.of("./logs/niffler-auth/app.log")));
        allureLifecycle.addAttachment("Niffler currency log", type, fileExtension, newInputStream(Path.of("./logs/niffler-currency/app.log")));
        allureLifecycle.addAttachment("Niffler-gateway log", type, fileExtension, newInputStream(Path.of("./logs/niffler-gateway/app.log")));
        allureLifecycle.addAttachment("Niffler-spend log", type, fileExtension, newInputStream(Path.of("./logs/niffler-spend/app.log")));
        allureLifecycle.addAttachment("Niffler-userdata log", type, fileExtension, newInputStream(Path.of("./logs/niffler-userdata/app.log")));
    }
}
