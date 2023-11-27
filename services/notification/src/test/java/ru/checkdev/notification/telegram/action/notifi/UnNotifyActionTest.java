package ru.checkdev.notification.telegram.action.notifi;

import org.assertj.core.api.Assertions;
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
import ru.checkdev.notification.telegram.action.notify.UnNotifyAction;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;

import static org.assertj.core.api.Assertions.assertThat;

class UnNotifyActionTest {
    /**
     * Поле заведено для отладки тестов
     * При указании данного email пользователя сервис бросает exception
     */
    private static final String ERROR_MAIL = "error@exception.er";
    /**
     * Поле заведено для отладки тестов
     * При указании данного ERROR_ID в качестве userId в моделях данных сервис бросает exception.
     */
    private static final String ERROR_ID = "-23";

    @Test
    void whenUnNotifyActionNotChatIdThenReturnMessageAccountNoReg() {
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
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        unNotifyAction.handle(update);
        BotApiMethod botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String expect = "Данный аккаунт Telegram на сайте не зарегистрирован";
        String actual = sendMessage.getText();
        Assertions.assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenUnNotifiActionChatIdIsPresentThenReturnMessageYouNotification() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        UserTelegram userTelegram = new UserTelegram(0, 1, chat.getId());
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String expect = "Вы отписались от уведомлений";
        String actual = sendMessage.getText();
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenUnNotifyActionServiceErrorThenReturnMessageYouNotification() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        var userTelegramService = new UserTelegramService(
                new UserTelegramRepositoryFake(
                        new SubscribeTopicRepositoryFake()
                ));
        UserTelegram userTelegram = new UserTelegram(0, Integer.parseInt(ERROR_ID), chat.getId());
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        UnNotifyAction unNotifyAction = new UnNotifyAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = unNotifyAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String expect = "Сервис не доступен попробуйте позже";
        String actual = sendMessage.getText();
        assertThat(actual).isEqualTo(expect);
    }
}