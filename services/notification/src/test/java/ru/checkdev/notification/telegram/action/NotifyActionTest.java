package ru.checkdev.notification.telegram.action;

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

class NotifyActionTest {

    @Test
    void whenNotChatId() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        SessionTg sessionTg = new SessionTg();
        message.setChat(chat);
        NotifyAction notifyAction = new NotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        notifyAction.handle(update);
        BotApiMethod<Message> botApiMethod = notifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenConnection() {
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
        NotifyAction notifyAction = new NotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = notifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Вы подписаны на уведомления";
        assertThat(text).isEqualTo(sendMessage.getText());
    }
}