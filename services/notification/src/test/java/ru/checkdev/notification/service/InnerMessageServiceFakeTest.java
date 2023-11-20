package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InnerMessageServiceFakeTest {

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        var innerMessageRepository = new InnerMessageRepositoryFake();
        var userTelegramRepositoryFake = new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake());
        var userTelegramService = new UserTelegramService(userTelegramRepositoryFake);
        var innerMessageService = new InnerMessageService(
                innerMessageRepository,
                userTelegramService
        );
        var botMessage = innerMessageService.saveMessage(
                new InnerMessage(1, 10, "text",
                        new Timestamp(System.currentTimeMillis()), false)
        );
        var result = innerMessageService.findByUserIdAndReadFalse(botMessage.getUserId());
        assertThat(result).contains(botMessage);
    }

    @Test
    public void whenSaveMessagesForSubscribers() {
        var categoryWithTopic = new CategoryWithTopicDTO(1, "Category_1",
                1, "Topic_1", 1, 3);
        var categorySubscribersIds = List.of(1);
        var topicSubscribersIds = List.of(2);
        var service = new InnerMessageService(new InnerMessageRepositoryFake(), null);
        service.saveMessagesForSubscribers(categoryWithTopic, categorySubscribersIds, topicSubscribersIds);
        var categoryMessages = service.findByUserIdAndReadFalse(1);
        var topicMessages = service.findByUserIdAndReadFalse(2);
        assertThat(1).isEqualTo(categoryMessages.size());
        assertThat("В категории \"Category_1\" появилось новое собеседование.")
                .isEqualTo(categoryMessages.get(0).getText());
        assertThat(1).isEqualTo(topicMessages.size());
        assertThat("Появилось новое собеседование по теме Topic_1.")
                .isEqualTo(topicMessages.get(0).getText());
    }
}