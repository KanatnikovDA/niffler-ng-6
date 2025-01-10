package guru.qa.niffler.jupiter.extension;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.model.allure.ScreenDif;
import io.qameta.allure.Allure;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Base64.Encoder encoder = Base64.getEncoder();

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class) &&
                parameterContext.getParameter()
                                .getType()
                                .isAssignableFrom(BufferedImage.class);
    }

    @SneakyThrows
    @Override
    public BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        ScreenShotTest screenShotTest = extensionContext
                .getRequiredTestMethod()
                .getAnnotation(ScreenShotTest.class);
        String imagePath = screenShotTest.value();
        return ImageIO.read(new ClassPathResource(imagePath).getInputStream());
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        ScreenShotTest screenShotTest = context.getRequiredTestMethod()
                                               .getAnnotation(ScreenShotTest.class);

        rewriteExpectedScreenshotIfNeeded(screenShotTest);
        addScreenshotDiffToAllure();

        throw throwable;
    }

    @SneakyThrows
    private void rewriteExpectedScreenshotIfNeeded(ScreenShotTest screenShotTest) {
        if (screenShotTest.rewriteExpected()) {
            BufferedImage actual = getActual();
            if (actual != null) {
                String filePath = "src/test/resources/" + screenShotTest.value();
                ImageIO.write(actual, "png", new File(filePath));
            }
        }
    }

    private void addScreenshotDiffToAllure() throws IOException {
        BufferedImage expected = getExpected();
        BufferedImage actual = getActual();
        BufferedImage diff = getDiff();

        ScreenDif screenDif = new ScreenDif(
                encodeImageToBase64(expected),
                encodeImageToBase64(actual),
                encodeImageToBase64(diff)
        );

        Allure.addAttachment(
                "Screenshot diff",
                "application/vnd.allure.image.diff",
                objectMapper.writeValueAsString(screenDif)
        );
    }

    private String encodeImageToBase64(BufferedImage image) throws IOException {
        if (image == null) {
            return null;
        }
        return "data:image/png;base64," + encoder.encodeToString(imageToBytes(image));
    }

    public static void setExpected(BufferedImage expected) {
        TestMethodContextExtension.context()
                                  .getStore(NAMESPACE)
                                  .put("expected", expected);
    }

    public static BufferedImage getExpected() {
        return TestMethodContextExtension.context()
                                         .getStore(NAMESPACE)
                                         .get("expected", BufferedImage.class);
    }

    public static void setActual(BufferedImage actual) {
        TestMethodContextExtension.context()
                                  .getStore(NAMESPACE)
                                  .put("actual", actual);
    }

    public static BufferedImage getActual() {
        return TestMethodContextExtension.context()
                                         .getStore(NAMESPACE)
                                         .get("actual", BufferedImage.class);
    }

    public static void setDiff(BufferedImage diff) {
        TestMethodContextExtension.context()
                                  .getStore(NAMESPACE)
                                  .put("diff", diff);
    }

    public static BufferedImage getDiff() {
        return TestMethodContextExtension.context()
                                         .getStore(NAMESPACE)
                                         .get("diff", BufferedImage.class);
    }

    private static byte[] imageToBytes(BufferedImage image) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
