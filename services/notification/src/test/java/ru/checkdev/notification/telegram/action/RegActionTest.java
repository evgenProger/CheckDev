package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Disabled;
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

class RegActionTest {

    @Test
    void whenHandleThenOk() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        RegAction regAction = new RegAction(new FakeTgCallConsole(), userTelegramService, innerMessageService, "www");
        BotApiMethod<Message> botApiMethod = regAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Введите email для регистрации:";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotUniqueChatId() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        UserTelegram userTelegram = new UserTelegram(88, 10, chat.getId());
        userTelegramService.save(userTelegram);
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        RegAction regAction = new RegAction(new FakeTgCallConsole(), userTelegramService, innerMessageService, "www");
        regAction.handle(message);
        BotApiMethod<Message> botApiMethod = regAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotCorrectEmail() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("емайл без собачки и точки");
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        RegAction regAction = new RegAction(new TgCallNoConnect(), userTelegramService, innerMessageService, "www");
        BotApiMethod<Message> botApiMethod = regAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Email: емайл без собачки и точки не корректный.%sпопробуйте снова.%s/new", n, n);
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        RegAction regAction = new RegAction(new FakeTgCallConsole(), userTelegramService, innerMessageService, "");
        BotApiMethod<Message> botApiMethod = regAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Сервис не доступен попробуйте позже%s/start", n);
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Disabled
    @Test
    void whenCallBackThenOk() {
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("mail@mail.ru");
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(), userTelegramService);
        RegAction regAction = new RegAction(new FakeTgCallConsole(), userTelegramService, innerMessageService, "www");
        BotApiMethod<Message> botApiMethod = regAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String expected = String.format(
                "Вы зарегистрированы: %s"
                        + "Имя: mail%s"
                        + "Email: mail@mail.ru%s"
                        + "Пароль : password%s"
                        + "urlSiteAuth", n, n, n, n);
        assertThat(sendMessage.getText()).isEqualTo(expected);
    }
}