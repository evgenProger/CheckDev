package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.service.UserTelegramService;

import java.util.Optional;

@AllArgsConstructor
public class RegAction15 implements Action {
    private final UserTelegramService userTelegramService;
    @Override
    public Optional<BotApiMethod> handle(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = "";
        if (userTelegramService.findByChatId(chatId).isPresent()) {
            text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
        } else {
            text = "Введите email для регистрации:";
        }
        return Optional.of(new SendMessage(chatId.toString(), text));
    }
}