package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.telegram.service.TgRunForService;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InnerMessageServiceFakeTest {

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        var innerMessageRepository = new InnerMessageRepositoryFake();
        var userTelegramRepositoryFake = new UserTelegramRepositoryFake();
        var userTelegramService = new UserTelegramService(userTelegramRepositoryFake);
        var tgRunForService = new TgRunForService();
        var innerMessageService = new InnerMessageService(
                innerMessageRepository,
                userTelegramService,
                tgRunForService
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
                1, "Topic_1", 1);
        var categorySubscribersIds = List.of(1);
        var topicSubscribersIds = List.of(2);
        var service = new InnerMessageService(new InnerMessageRepositoryFake(), null, null);
        service.saveMessagesForSubscribers(categoryWithTopic, categorySubscribersIds, topicSubscribersIds);
        var categoryMessages = service.findByUserIdAndReadFalse(1);
        var topicMessages = service.findByUserIdAndReadFalse(2);
        assertEquals(1, categoryMessages.size());
        assertEquals("В категории \"Category_1\" появилось новое собеседование.",
                categoryMessages.get(0).getText());
        assertEquals(1, topicMessages.size());
        assertEquals("Появилось новое собеседование по теме Topic_1.",
                topicMessages.get(0).getText());
    }
}