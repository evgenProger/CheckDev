package ru.checkdev.notification.web;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.service.SubscribeTopicService;
import static org.assertj.core.api.Assertions.assertThat;

public class SubscribeTopicControllerFakeTest {

    @Test
    public void whenFindTopicByUserId() {
        var subscribeTopic = new SubscribeTopic(1, 2, 2);
        var service = new SubscribeTopicService(new SubscribeTopicRepositoryFake());
        service.save(subscribeTopic);
        var resp = new SubscribeTopicController(service)
                .findTopicByUserId(subscribeTopic.getUserId());
        assertThat(resp.getBody()).containsOnly(subscribeTopic.getUserId());
    }
}