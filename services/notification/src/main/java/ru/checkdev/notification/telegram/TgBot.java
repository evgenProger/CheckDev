package ru.checkdev.notification.telegram;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.checkdev.notification.telegram.action.Action;
import ru.checkdev.notification.telegram.action.info.UnKnownRequestAction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyIterator;
import static java.util.Collections.emptyList;

/**
 * Реализация меню телеграм бота.
 * Класс TgBoot для отправки и получения сообщений с телеграмм ботом,
 * используется для профиля default, c использования Telegram API
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
public class TgBot extends TelegramLongPollingBot implements Bot {
    private final Map<String, Iterator<Action>> bindingBy = new ConcurrentHashMap<>();
    private final Map<String, List<Action>> actions;
    private final String username;
    private final String token;

    public TgBot(Map<String, List<Action>> actions, String username, String token) {
        this.actions = actions;
        this.username = username;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public void onUpdateReceived(Update update) {
        if (!update.hasMessage()) {
            return;
        }
        var key = update.getMessage().getText();
        var chatId = update.getMessage().getChatId().toString();
        if (actions.containsKey(key)) {
            bindingBy.put(chatId, actions.get(key).iterator());
        } else if (!bindingBy.getOrDefault(chatId, emptyIterator()).hasNext()) {
            var msg = new UnKnownRequestAction().handle(update);
            send(msg.get());
            return;
        }
        if (!bindingBy.containsKey(chatId)) {
            return;
        }
        var bindingActions = bindingBy.get(chatId);
        if (bindingActions == null || !bindingActions.hasNext()) {
            bindingBy.remove(chatId);
            return;
        }
        Optional<BotApiMethod> result;
        do {
            result = bindingActions.next().handle(update);
        } while (result.isEmpty());
        send(result.get());
    }

    public void send(BotApiMethod msg) {
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}