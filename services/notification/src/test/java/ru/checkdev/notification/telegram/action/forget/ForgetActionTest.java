package ru.checkdev.notification.telegram.action.forget;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;

import static org.assertj.core.api.Assertions.assertThat;

class ForgetActionTest {
    /**
     * Поле заведено для отладки тестов
     * При указании данного ERROR_ID в качестве userId в моделях данных сервис бросает exception.
     */
    private static final String ERROR_ID = "-23";

    private final UserTelegramService userTelegramService = new UserTelegramService(
            new UserTelegramRepositoryFake(
                    new SubscribeTopicRepositoryFake()
            ));

    @Test
    void whenForgetActionNotChatIdThenMessageAccountNotRegistered() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        message.setChat(chat);
        ForgetAction forgetAction = new ForgetAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        forgetAction.handle(update);
        BotApiMethod<Message> botApiMethod = forgetAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenForgetActionChatIdIsPresentThenMessageNewPassword() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        UserTelegram userTelegram = new UserTelegram(0, 1, chat.getId());
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        message.setText("password");
        ForgetAction forgetAction = new ForgetAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = forgetAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String sl = System.lineSeparator();
        var actual = sendMessage.getText();
        String passInMessage = getPassInMessage(actual);
        String text = "Ваш новый пароль:" + sl
                + passInMessage;
        assertThat(text).isEqualTo(actual);
    }

    @Test
    void whenForgetActionWebClientExceptionThenMessageServiceError() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        UserTelegram userTelegram = new UserTelegram(0, Integer.parseInt(ERROR_ID), chat.getId());
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        message.setText("password");
        ForgetAction forgetAction = new ForgetAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = forgetAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        var actual = sendMessage.getText();
        var expect = "Сервис не доступен попробуйте позже";
        assertThat(actual).isEqualTo(expect);
    }

    private String getPassInMessage(String textMessage) {
        int startPassIndex = textMessage.lastIndexOf("tg/");
        return textMessage.substring(startPassIndex, textMessage.length());
    }
}