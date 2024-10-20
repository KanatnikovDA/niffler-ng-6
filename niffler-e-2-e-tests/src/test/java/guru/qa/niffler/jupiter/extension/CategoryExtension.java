package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.*;

public class CategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                         .ifPresent(userAnnotation -> {
                             if (userAnnotation.categories().length > 0) {
                                 Category category = userAnnotation.categories()[0];
                                 CategoryJson categoryJson = new CategoryJson(
                                         null,
                                         randomCategoryName(),
                                         randomUsername(),
                                         category.archived()
                                 );
                                 CategoryJson created = spendApiClient.createCategory(categoryJson);
                                 if (category.archived()) {
                                     CategoryJson archivedCategory = new CategoryJson(
                                             created.id(),
                                             created.name(),
                                             created.username(),
                                             true
                                     );
                                     created = spendApiClient.updateCategory(archivedCategory);
                                 }
                                 context.getStore(NAMESPACE)
                                        .put(
                                                context.getUniqueId(),
                                                created
                                        );
                             }
                         });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(NAMESPACE)
                                       .get(context.getUniqueId(), CategoryJson.class);
        if (!Objects.isNull(category) && !category.archived()) {
            category = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            spendApiClient.updateCategory(category);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter()
                               .getType()
                               .isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE)
                               .get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
