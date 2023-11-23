package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.service.UserTelegramService;

import java.util.Optional;

/**
 * 3. Мидл
 * Класс реализует пункт меню регистрации нового пользователя в телеграм бот
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 * Arcady555
 * 06.11.2023
 */
@AllArgsConstructor
@Slf4j
public class RegAction10 implements Action {
    private final UserTelegramService userTelegramService;

    @Override
    public Optional<BotApiMethod> handle(Update update) {
        var msg = update.getMessage();
        var text = "";
        var chatId = msg.getChatId();
        if (userTelegramService.findByChatId(chatId).isPresent()) {
            text = "Данный аккаунт Telegram уже зарегистрирован на сайте";
        } else {
            text = "Введите имя для регистрации нового пользователя:";
        }
        return Optional.of(new SendMessage(chatId.toString(), text));
    }
}
