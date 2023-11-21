package ru.checkdev.notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeCategory;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscribeCategoryRepositoryTest {

    private final SubscribeCategoryRepository repository = new SubscribeCategoryRepositoryFake();

    private SubscribeCategory subscribeCategory;

    @BeforeEach
    void init() {
        repository.deleteAll();
        subscribeCategory = new SubscribeCategory(0, 1, 1);
        repository.save(subscribeCategory);
    }

    @Test
    void whenFindSubscribeCategoryByUserId() {
        var result = repository.findByUserId(1);
        assertThat(result.isEmpty()).isEqualTo(false);
        assertThat(1).isEqualTo(result.size());
        assertThat(result.get(0)).isEqualTo(subscribeCategory);
    }

    @Test
    void whenSubscribeCategoryNotFoundByUserId() {
        assertThat(repository.findByUserId(2).isEmpty()).isEqualTo(true);
    }

    @Test
    void whenFindByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndCategoryId(1, 1);
        assertThat(result != null).isEqualTo(true);
        assertThat(result).isEqualTo(subscribeCategory);
    }

    @Test
    void whenNotFoundByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndCategoryId(2, 2);
        assertThat(result != null).isEqualTo(false);
    }
}
