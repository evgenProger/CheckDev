package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;
import ru.checkdev.notification.telegram.service.TgCallNoConnect;

import static org.assertj.core.api.Assertions.assertThat;


class NotifyActionTest {

    @Test
    void whenNotChatId() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        NotifyAction notifyAction = new NotifyAction(new FakeTgCallConsole(), userTelegramService,
                new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService));
        notifyAction.handle(message);
        BotApiMethod<Message> botApiMethod = notifyAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(11, 10, chat.getId());
        userTelegramService.save(userTelegram);
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        NotifyAction notifyAction = new NotifyAction(new TgCallNoConnect(), userTelegramService,
                new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService));
        BotApiMethod<Message> botApiMethod = notifyAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Сервис не доступен попробуйте позже";
        assertThat(text).isEqualTo(sendMessage.getText());
    }
}