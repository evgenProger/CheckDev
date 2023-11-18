package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.service.TgCall;

import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
@Disabled
class RegActionTest {

    @MockBean
    private UserTelegramService userTelegramService;

    @MockBean
    private InnerMessageService messageService;

    @Autowired
    private TgCall tgCall;

    @Test
    void whenHandleThenOk() {
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        RegAction regAction = new RegAction(tgCall, userTelegramService, messageService, "www");
        BotApiMethod<Message> botApiMethod = regAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Введите email для регистрации:";
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotUniqueChatId() {
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        UserTelegram userTelegram = new UserTelegram(88, 10, chat.getId());
        when(userTelegramService.findByChatId(chat.getId())).thenReturn(Optional.of(userTelegram));
        RegAction regAction = new RegAction(tgCall, userTelegramService, messageService, "www");
        regAction.handle(message);
        BotApiMethod<Message> botApiMethod = regAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotCorrectEmail() {
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("емайл без собачки и точки");
        RegAction regAction = new RegAction(tgCall, userTelegramService, messageService, "www");
        BotApiMethod<Message> botApiMethod = regAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Email: емайл без собачки и точки не корректный.%sпопробуйте снова.%s/new", n, n);
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        RegAction regAction = new RegAction(tgCall, userTelegramService, messageService, "");
        BotApiMethod<Message> botApiMethod = regAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Сервис не доступен попробуйте позже%s/start", n);
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Disabled
    @Test
    void whenCallBackThenOk() {
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("mail@mail.ru");
        RegAction regAction = new RegAction(tgCall, userTelegramService, messageService, "www");
        BotApiMethod<Message> botApiMethod = regAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format(
                "Вы зарегистрированы: %s"
                        + "Имя: mail%s"
                        + "Email: mail@mail.ru%s"
                        + "Пароль : password%s"
                        + "urlSiteAuth", n, n, n, n);
        Assertions.assertEquals(text, sendMessage.getText());
    }
}