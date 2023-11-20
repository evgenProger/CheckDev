package ru.checkdev.notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeTopic;

import static org.assertj.core.api.Assertions.assertThat;

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
    void whenFindSubscribeCategoryByUserId() {
        var result = repository.findByUserId(1);
        assertThat(result.isEmpty()).isEqualTo(false);
        assertThat(1).isEqualTo(result.size());
        assertThat(result.get(0)).isEqualTo(subscribeTopic);
    }

    @Test
    void whenSubscribeCategoryNotFoundByUserId() {
        assertThat(repository.findByUserId(2).isEmpty()).isTrue();
    }

    @Test
    void whenFindByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndTopicId(1, 1);
        assertThat(result != null).isEqualTo(true);
        assertThat(result).isEqualTo(subscribeTopic);
    }

    @Test
    void whenNotFoundByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndTopicId(2, 2);
        assertThat(result != null).isEqualTo(false);
    }
}
