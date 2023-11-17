package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTelegramServiceFakeTest {
    @Test
    void save() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake());
        var chatId = 333L;
        service.save(new UserTelegram(11, 10, chatId));
        var userTg = service.findByChatId(chatId);
        assertThat(userTg.isPresent()).isTrue();
        assertThat(userTg.get().getUserId()).isEqualTo(10);
    }
}