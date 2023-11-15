package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.telegram.service.TgRunForService;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

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
}