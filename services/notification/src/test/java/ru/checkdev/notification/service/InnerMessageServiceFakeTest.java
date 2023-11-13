package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class InnerMessageServiceFakeTest {

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        var innerMessageRepository = new InnerMessageRepositoryFake();
        var innerMessageService = new InnerMessageService(innerMessageRepository);
        var botMessage = innerMessageService.saveMessage(
                new InnerMessage(1, 10, "text",
                new Timestamp(System.currentTimeMillis()), false)
        );
        var result = innerMessageService.findByUserIdAndReadFalse(botMessage.getUserId());
        assertThat(result).contains(botMessage);
    }
}