package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.TgCall;

import static org.assertj.core.api.Assertions.assertThat;

class UnNotifyActionTest {

    @Test
    void whenNotChatId() {
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        update.getMessage().setChat(chat);
        when(userTelegramService.findByChatId(chat.getId())).thenReturn(Optional.empty());
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, tgCall, userTelegramService);
        unNotifyAction.handle(update);
        BotApiMethod botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        assertThat(sendMessage.getText()).isEqualTo("Вы отписались от уведомлений");
    }

    @Test
    void whenNotConnection() {
        Update update = new Update();
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(0, 10, chat.getId());
        userTelegramService.save(userTelegram);
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        when(messageService.saveMessage(any(InnerMessage.class))).thenThrow(new RuntimeException());
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, tgCall, userTelegramService);
        BotApiMethod botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        assertThat(sendMessage.getText()).isEqualTo("Сервис не доступен попробуйте позже");
    }
}