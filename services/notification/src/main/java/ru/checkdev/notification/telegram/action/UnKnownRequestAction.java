package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@AllArgsConstructor
public class UnKnownRequestAction implements Action {
    @Override
    public Optional<BotApiMethod> handle(Update update) {
        var chatId = update.getMessage().getChatId().toString();
        String sl = System.lineSeparator();
        var out = new StringBuilder();
        out.append("Команда не поддерживается! Список доступных команд: ")
                .append(sl).append("/start");
        return Optional.of(new SendMessage(chatId, out.toString()));
    }
}