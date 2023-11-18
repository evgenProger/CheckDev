package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.service.TgCall;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NtfSrv.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Disabled
class NotifyActionTest {
    @MockBean
    private UserTelegramService userTelegramService;

    @MockBean
    private InnerMessageService messageService;

    @Autowired
    private TgCall tgCall;

    @Test
    void whenNotChatId() {
        Chat chat = new Chat(1L, "type");
        when(userTelegramService.findByChatId(chat.getId())).thenReturn(Optional.empty());
        Message message = new Message();
        message.setChat(chat);
        NotifyAction notifyAction = new NotifyAction(tgCall, userTelegramService, messageService);
        notifyAction.handle(message);
        BotApiMethod<Message> botApiMethod = notifyAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(11, 10, chat.getId());
        when(userTelegramService.findByChatId(userTelegram.getChatId())).thenReturn(Optional.of(userTelegram));
        when(messageService.saveMessage(any(InnerMessage.class))).thenThrow(new RuntimeException());
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        NotifyAction notifyAction = new NotifyAction(tgCall, userTelegramService, messageService);
        BotApiMethod<Message> botApiMethod = notifyAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Сервис не доступен попробуйте позже";
        Assertions.assertEquals(text, sendMessage.getText());
    }
}