package ru.checkdev.notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeCategory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    void checkThatStuffNotNull() {
        assertNotNull(repository);
    }

    @Test
    void whenFindSubscribeCategoryByUserId() {
        var result = repository.findByUserId(1);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertThat(result.get(0)).isEqualTo(subscribeCategory);
    }

    @Test
    void whenSubscribeCategoryNotFoundByUserId() {
        assertTrue(repository.findByUserId(2).isEmpty());
    }

    @Test
    void whenFindByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndCategoryId(1, 1);
        assertNotNull(result);
        assertThat(result).isEqualTo(subscribeCategory);
    }

    @Test
    void whenNotFoundByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndCategoryId(2, 2);
        assertNull(result);
    }
}
