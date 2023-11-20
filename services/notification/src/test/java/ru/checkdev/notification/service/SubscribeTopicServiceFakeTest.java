package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscribeTopicServiceFakeTest {

    @Test
    public void whenGetAllSubTopicReturnContainsValue() {
        var service = new SubscribeTopicService(new SubscribeTopicRepositoryFake());
        SubscribeTopic subscribeTopic = service.save(new SubscribeTopic(0, 1, 1));
        List<SubscribeTopic> result = service.findAll();
        assertThat(result).contains(subscribeTopic);
    }

    @Test
    public void requestByUserIdReturnCorrectValue() {
        var service = new SubscribeTopicService(new SubscribeTopicRepositoryFake());
        SubscribeTopic subscribeTopic = service.save(new SubscribeTopic(1, 2, 2));
        List<Integer> result = service.findTopicByUserId(subscribeTopic.getUserId());
        assertThat(result).contains(2);
    }

    @Test
    public void whenDeleteTopicCatItIsNotExist() {
        var service = new SubscribeTopicService(new SubscribeTopicRepositoryFake());
        SubscribeTopic subscribeTopic = service.save(new SubscribeTopic(2, 3, 3));
        subscribeTopic = service.delete(subscribeTopic);
        List<SubscribeTopic> result = service.findAll();
        assertThat(result).doesNotContain(subscribeTopic);
    }

    @Test
    public void whenFindUserIdsByTopicId() {
        var service = new SubscribeTopicService(new SubscribeTopicRepositoryFake());
        SubscribeTopic subscribeTopic1 = service
                .save(new SubscribeTopic(1, 1, 1));
        SubscribeTopic subscribeTopic2 = service
                .save(new SubscribeTopic(2, 2, 1));
        var result = service.findUserIdsByTopicIdExcludeCurrent(1, 3);
        assertThat(2).isEqualTo(result.size());
        service.delete(subscribeTopic1);
        service.delete(subscribeTopic2);
    }

    @Test
    public void whenFindUserIdsByTopicIdExcludeFirst() {
        var service = new SubscribeTopicService(new SubscribeTopicRepositoryFake());
        SubscribeTopic subscribeTopic1 = service
                .save(new SubscribeTopic(1, 1, 1));
        SubscribeTopic subscribeTopic2 = service
                .save(new SubscribeTopic(2, 2, 1));
        var result = service.findUserIdsByTopicIdExcludeCurrent(1, 1);
        assertThat(1).isEqualTo(result.size());
        service.delete(subscribeTopic1);
        service.delete(subscribeTopic2);
    }
}