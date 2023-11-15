package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
class ForgetActionTest {
    @Autowired
    private UserTelegramService userTelegramService;

    @Autowired
    private InnerMessageService messageService;

    @Mock
    private TgCall tgCall;

    @Test
    void whenNotChatId() {
        Chat chat = new Chat(1L, "type");
        userTelegramService.delete(1);
        Message message = new Message();
        message.setChat(chat);
        ForgetAction forgetAction = new ForgetAction(tgCall, userTelegramService, messageService);
        forgetAction.handle(message);
        BotApiMethod<Message> botApiMethod = forgetAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
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
        ForgetAction forgetAction = new ForgetAction(tgCall, userTelegramService, messageService);
        BotApiMethod<Message> botApiMethod = forgetAction.callback(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Сервис не доступен попробуйте позже";
        Assertions.assertEquals(text, sendMessage.getText());
        userTelegramService.delete(1);
    }
}