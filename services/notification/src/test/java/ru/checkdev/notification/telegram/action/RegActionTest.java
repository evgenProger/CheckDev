package ru.checkdev.notification.telegram.action;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;


class RegActionTest {

    @Test
    void whenHandleThenOk() {
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
        RegAction10 regAction10 = new RegAction10(userTelegramService);
        BotApiMethod botApiMethod = regAction10.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Введите имя для регистрации нового пользователя:";
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenHandleThenOk20() {
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
        RegAction15 regAction15 = new RegAction15(userTelegramService);
        BotApiMethod botApiMethod = regAction15.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Введите email для регистрации:";
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotCorrectEmail() {
        Update update = new Update();
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("емайл без собачки и точки");
        update.setMessage(message);
        RegAction20 regAction10 = new RegAction20(sessionTg);
        BotApiMethod botApiMethod = regAction10.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = "Email:  не корректный." + n
                + "попробуйте снова." + n
                + "/new";
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        update.setMessage(message);
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        RegAction30 regAction10 = new RegAction30(sessionTg, new FakeTgCallConsole(), userTelegramService, "www");
        BotApiMethod botApiMethod = regAction10.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = "Пройдите регистрацию заново" + n + "/new";
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    @Disabled
    void whenCallBackThenOk() {
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        message.setChat(chat);
        message.setText("mail@mail.ru");
        update.setMessage(message);
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        RegAction30 regAction30 = new RegAction30(sessionTg, new FakeTgCallConsole(), userTelegramService, "www");
        BotApiMethod botApiMethod = regAction30.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format(
                "Вы зарегистрированы: %s"
                        + "Имя: mail%s"
                        + "Email: mail@mail.ru%s"
                        + "Пароль : password%s"
                        + "urlSiteAuth", n, n, n, n);
        Assertions.assertThat(text).isEqualTo(sendMessage.getText());
    }
}
