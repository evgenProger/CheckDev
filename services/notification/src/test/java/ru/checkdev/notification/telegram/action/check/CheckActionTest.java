package ru.checkdev.notification.telegram.action.check;

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

class CheckActionTest {
    private UserTelegramService userTelegramService = new UserTelegramService(
            new UserTelegramRepositoryFake(
                    new SubscribeTopicRepositoryFake()));

    @Test
    void whenNotChatId() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        message.setChat(chat);
        CheckAction checkAction = new CheckAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        checkAction.handle(update);
        BotApiMethod<Message> botApiMethod = checkAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenHandleChatIdIsPresentThenReturnMessage() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        UserTelegram userTelegram = new UserTelegram(0, 1, chat.getId(), false);
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        CheckAction checkAction = new CheckAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = checkAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String sl = System.lineSeparator();
        String text = "Имя:" + sl
                + "FakeName" + sl
                + "Email:" + sl
                + "FakeEmail" + sl;
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenHandleChatIdIsPresentThenReturnServiceError() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        UserTelegram userTelegram = new UserTelegram(0, -23, chat.getId(), false);
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        CheckAction checkAction = new CheckAction(sessionTg, new FakeTgCallConsole(), userTelegramService);
        BotApiMethod<Message> botApiMethod = checkAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Сервис не доступен попробуйте позже";
        assertThat(text).isEqualTo(sendMessage.getText());
    }
}