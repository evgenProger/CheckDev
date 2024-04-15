package ru.checkdev.notification.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.telegram.Bot;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationMessageTgTest {

    @Mock
    private Bot mockBot;
    private NotificationMessageTg service;

    @BeforeEach
    void setUp() {
        service = new NotificationMessageTg(mockBot);
    }

    @Test
    void whenSendMessageAndTargetListIsEmptyThenGetEmptyList() {
        List<UserTelegram> targets = new ArrayList<>();
        String message = "message";
        assertThat(service.sendMessage(targets, message)).isEmpty();
    }

    @Test
    void whenSendMessageThenGetListOfInnerMessagesAndReadValueTrueIfUserNotifiedByBot() {
        UserTelegram userFirstNotifiable = new UserTelegram(1, 1, 1111L, true);
        UserTelegram userFirstUnNotifiable = new UserTelegram(2, 2, 2222L, false);
        UserTelegram userSecondNotifiable = new UserTelegram(3, 3, 3333L, true);
        String message = "message";
        List<UserTelegram> targets = List.of(
                userFirstNotifiable, userFirstUnNotifiable, userSecondNotifiable
        );

        List<InnerMessage> expected = List.of(
                createInnerMessage(userFirstNotifiable, message, userFirstNotifiable.isNotifiable()),
                createInnerMessage(userFirstUnNotifiable, message, userFirstUnNotifiable.isNotifiable()),
                createInnerMessage(userSecondNotifiable, message, userSecondNotifiable.isNotifiable())
        );
        List<InnerMessage> actual = service.sendMessage(targets, message);

        verify(mockBot, times(2)).send(any(BotApiMethod.class));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenSendMessageAndUserNotifiableThenGetInnerMessagesAndReadValueTrue() {
        UserTelegram target = new UserTelegram(1, 1, 1111L, true);
        String message = "message";

        InnerMessage expected = createInnerMessage(target, message, target.isNotifiable());
        InnerMessage actual = service.sendMessage(target, message);

        verify(mockBot, times(1)).send(any(BotApiMethod.class));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void whenSendMessageAndUserNotNotifiableThenGetInnerMessagesAndReadValueFalse() {
        UserTelegram target = new UserTelegram(1, 1, 1111L, false);
        String message = "message";

        InnerMessage expected = createInnerMessage(target, message, target.isNotifiable());
        InnerMessage actual = service.sendMessage(target, message);

        verify(mockBot, times(0)).send(any(BotApiMethod.class));
        assertThat(actual).isEqualTo(expected);
    }

    private InnerMessage createInnerMessage(UserTelegram user, String message, boolean read) {
        return InnerMessage.of()
                .userId(user.getUserId())
                .text(message)
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(read)
                .build();
    }

}