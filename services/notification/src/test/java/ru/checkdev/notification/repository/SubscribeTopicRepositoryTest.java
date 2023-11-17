package ru.checkdev.notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeTopic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class SubscribeTopicRepositoryTest {

    private final SubscribeTopicRepository repository = new SubscribeTopicRepositoryFake();

    private SubscribeTopic subscribeTopic;

    @BeforeEach
    void init() {
        subscribeTopic = new SubscribeTopic(0, 1, 1);
        repository.deleteAll();
        repository.save(subscribeTopic);
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
        assertThat(result.get(0)).isEqualTo(subscribeTopic);
    }

    @Test
    void whenSubscribeCategoryNotFoundByUserId() {
        assertTrue(repository.findByUserId(2).isEmpty());
    }

    @Test
    void whenFindByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndTopicId(1, 1);
        assertNotNull(result);
        assertThat(result).isEqualTo(subscribeTopic);
    }

    @Test
    void whenNotFoundByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndTopicId(2, 2);
        assertNull(result);
    }
}
