package ru.checkdev.notification.telegram.action.info;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.StringJoiner;

import static org.assertj.core.api.Assertions.assertThat;

class InfoActionTest {
    @Test
    void whenInfoActionHandleThenReturnMessageMenu() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        List<String> actions = List.of(
                "/start",
                "/new  зарегистрировать нового пользователя");
        InfoAction infoAction = new InfoAction(actions);
        BotApiMethod<Message> botApiMethod = infoAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String expect = new StringJoiner(ls, "", ls)
                .add("Доступные команды:")
                .add("/start")
                .add("/new  зарегистрировать нового пользователя").toString();
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenInfoActionHandleThenReturnMessageAllMenu() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        List<String> actions = List.of(
                "/start    - Доступные команды",
                "/new      - Зарегистрировать нового пользователя",
                "/check    - Связанный аккаунт",
                "/forget   - Восстановить пароль",
                "/notify   - Подписаться на уведомления",
                "/unnotify - Отписаться от уведомлений",
                "/bind     - Привязать аккаунт CheckDev к данному аккаунту Telegram",
                "/unbind   - Отвязать аккаунт CheckDev от данного аккаунта Telegram");
        InfoAction infoAction = new InfoAction(actions);
        BotApiMethod<Message> botApiMethod = infoAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String expect = new StringJoiner(ls, "", ls)
                .add("Доступные команды:")
                .add("/start    - Доступные команды")
                .add("/new      - Зарегистрировать нового пользователя")
                .add("/check    - Связанный аккаунт")
                .add("/forget   - Восстановить пароль")
                .add("/notify   - Подписаться на уведомления")
                .add("/unnotify - Отписаться от уведомлений")
                .add("/bind     - Привязать аккаунт CheckDev к данному аккаунту Telegram")
                .add("/unbind   - Отвязать аккаунт CheckDev от данного аккаунта Telegram").toString();
        assertThat(actual).isEqualTo(expect);
    }
}