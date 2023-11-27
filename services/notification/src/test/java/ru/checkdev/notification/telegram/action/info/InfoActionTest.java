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
                .add("Выберите действие:")
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
                "/start",
                "/new  зарегистрировать нового пользователя",
                "/check  получить своё имя и Email",
                "/forget  генерация нового пароля",
                "/notify  подписаться на уведомления",
                "/unnotify  отписаться от уведомлений");
        InfoAction infoAction = new InfoAction(actions);
        BotApiMethod<Message> botApiMethod = infoAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String expect = new StringJoiner(ls, "", ls)
                .add("Выберите действие:")
                .add("/start")
                .add("/new  зарегистрировать нового пользователя")
                .add("/check  получить своё имя и Email")
                .add("/forget  генерация нового пароля")
                .add("/notify  подписаться на уведомления")
                .add("/unnotify  отписаться от уведомлений").toString();
        assertThat(actual).isEqualTo(expect);
    }
}