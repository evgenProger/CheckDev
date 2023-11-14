package ru.checkdev.notification.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class SubscribeTopicServiceTest {
    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @Autowired
    private SubscribeTopicService service;


    @Test
    public void checkContext() {
        assertThat(service).isNotNull();
    }


    @Test
    public void whenGetAllSubTopicReturnContainsValue() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(0, 1, 1));
        List<SubscribeTopic> result = this.service.findAll();
        assertThat(result).contains(subscribeTopic);
    }

    @Test
    public void requestByUserIdReturnCorrectValue() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(1, 2, 2));
        List<Integer> result = this.service.findTopicByUserId(subscribeTopic.getUserId());
        assertThat(result).contains(2);
    }

    @Test
    public void whenDeleteTopicCatItIsNotExist() {
        SubscribeTopic subscribeTopic = this.service.save(new SubscribeTopic(2, 3, 3));
        subscribeTopic = this.service.delete(subscribeTopic);
        List<SubscribeTopic> result = this.service.findAll();
        assertThat(result).doesNotContain(subscribeTopic);
    }

    @Test
    public void whenFindUserIdsByTopicId() {
        SubscribeTopic subscribeTopic1 = service
                .save(new SubscribeTopic(1, 1, 1));
        SubscribeTopic subscribeTopic2 = service
                .save(new SubscribeTopic(2, 2, 1));
        var result = service.findUserIdsByTopicId(1);
        Assertions.assertEquals(2, result.size());
        service.delete(subscribeTopic1);
        service.delete(subscribeTopic2);
    }
}