package ru.checkdev.notification.telegram.action;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;

import static org.assertj.core.api.Assertions.assertThat;

class UnNotifyActionTest {

    @Test
    void whenNotChatId() {
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        unNotifyAction.handle(update);
        BotApiMethod botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        UserTelegram userTelegram = new UserTelegram(11, 10, chat.getId());
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        message.setText("a@a.ru");
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String sl = System.lineSeparator();
        String text = "Вы отписались от уведомлений";
        assertThat(text).isEqualTo(sendMessage.getText());
    }
}