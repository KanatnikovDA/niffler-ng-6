package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendRepository {

    SpendEntity create(SpendEntity user);

    SpendEntity update(SpendEntity user);

    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndSpendName(String username, String name);

    Optional<SpendEntity> findById(UUID id);

    List<SpendEntity> findByUsernameAndSpendDescription(String username, String description);

    void remove(SpendEntity user);

    void removeCategory(CategoryEntity user);
}
