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

class CheckActionTest {

    @Test
    void whenNotChatId() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        userTelegramService.delete(1);
        Message message = new Message();
        message.setChat(chat);
        CheckAction checkAction = new CheckAction(new FakeTgCallConsole(), userTelegramService,
                new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService));
        checkAction.handle(message);
        BotApiMethod<Message> botApiMethod = checkAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Данный аккаунт Telegram на сайте не зарегистрирован%s", n);
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(0, 10, chat.getId());
        userTelegramService.save(userTelegram);
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        CheckAction checkAction = new CheckAction(new TgCallNoConnect(), userTelegramService,
                new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService));
        BotApiMethod<Message> botApiMethod = checkAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Сервис не доступен попробуйте позже%s", n);
        assertThat(text).isEqualTo(sendMessage.getText());
        userTelegramService.delete(1);
    }
}