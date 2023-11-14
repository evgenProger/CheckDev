package ru.checkdev.notification.telegram.action;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;

class UnKnownRequestActionTest {
    @Test
    void handle() {
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        UnKnownRequestAction unKnownRequestAction = new UnKnownRequestAction();
        BotApiMethod<Message> botApiMethod = unKnownRequestAction.handle(message);
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = String.format(
                "Команда не поддерживается! Список доступных команд: %s/start",
                System.lineSeparator());
        Assertions.assertEquals(text, sendMessage.getText());
    }
}