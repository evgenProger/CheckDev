package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.telegram.service.TgCall;

@TestPropertySource(locations="classpath:application.properties")
@SpringBootTest(classes = NtfSrv.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class CheckActionTest {

    @Autowired
    private UserTelegramService userTelegramService;

    @Autowired
    private InnerMessageService messageService;

    @Autowired
    private TgCall tgCall;

    @Test
    void whenNotChatId() {
        Chat chat = new Chat(1L, "type");
        userTelegramService.delete(1);
        Message message = new Message();
        message.setChat(chat);
        CheckAction checkAction = new CheckAction(tgCall, userTelegramService, messageService);
        checkAction.handle(message);
        BotApiMethod<Message> botApiMethod = checkAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Данный аккаунт Telegram на сайте не зарегистрирован%s", n);
        Assertions.assertEquals(text, sendMessage.getText());
    }

    @Test
    void whenNotConnection() {
        Chat chat = new Chat(1L, "type");
        UserTelegram userTelegram = new UserTelegram(0, 10, chat.getId());
        userTelegramService.save(userTelegram);
        Message message = new Message();
        message.setChat(chat);
        message.setText("a@a.ru");
        CheckAction checkAction = new CheckAction(tgCall, userTelegramService, messageService);
        BotApiMethod<Message> botApiMethod = checkAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String n = System.lineSeparator();
        String text = String.format("Сервис не доступен попробуйте позже%s", n);
        Assertions.assertEquals(text, sendMessage.getText());
        userTelegramService.delete(1);
    }
}