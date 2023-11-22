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
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.TgCall;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
@Disabled
class UnNotifyActionTest {
    @MockBean
    private UserTelegramService userTelegramService;

    @MockBean
    private InnerMessageService messageService;

    @Autowired
    private TgCall tgCall;

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
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        Update update = new Update();
        SessionTg sessionTg = new SessionTg();
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(0, 10, chat.getId());
        when(userTelegramService.findByChatId(chat.getId())).thenReturn(Optional.of(userTelegram));
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        when(messageService.saveMessage(any(InnerMessage.class))).thenThrow(new RuntimeException());
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, tgCall, userTelegramService);
        BotApiMethod botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Сервис не доступен попробуйте позже";
        Assertions.assertEquals(text, sendMessage.getText());
    }
}