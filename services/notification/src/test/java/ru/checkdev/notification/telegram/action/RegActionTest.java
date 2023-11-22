package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.TgCall;

import static org.assertj.core.api.Assertions.assertThat;

class RegActionTest {

    @Test
    void whenHandleThenOk() {
        Update update = new Update();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        RegAction10 regAction10 = new RegAction10(userTelegramService);
        BotApiMethod botApiMethod = regAction10.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Введите имя для регистрации нового пользователя:";
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotUniqueChatId() {
        Update update = new Update();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        UserTelegram userTelegram = new UserTelegram(88, 10, chat.getId());
        when(userTelegramService.findByChatId(chat.getId())).thenReturn(Optional.of(userTelegram));
        RegAction15 regAction15 = new RegAction15(userTelegramService);
        BotApiMethod botApiMethod = regAction15.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotCorrectEmail() {
        Update update = new Update();
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("емайл без собачки и точки");
        RegAction20 regAction10 = new RegAction20(sessionTg);
        BotApiMethod botApiMethod = regAction10.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Email: емайл без собачки и точки не корректный.%sпопробуйте снова.%s/new", n, n);
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        Update update = new Update();
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        RegAction30 regAction10 = new RegAction30(sessionTg, tgCall, userTelegramService, "www");
        BotApiMethod botApiMethod = regAction10.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Сервис не доступен попробуйте позже%s/start", n);
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Disabled
    @Test
    void whenCallBackThenOk() {
        Update update = new Update();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("mail@mail.ru");
        RegAction10 regAction10 = new RegAction10(userTelegramService);
        BotApiMethod botApiMethod = regAction10.handle(update).get();
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