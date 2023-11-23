package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.TgCall;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class UnNotifyAction implements Action {
    private static final String URL_AUTH_UNNOTIFIED = "/profiles/tg/unnotified/";
    private final SessionTg sessionTg;
    private final TgCall tgCall;
    private final UserTelegramService userTelegramService;

    @Override
    public Optional<BotApiMethod> handle(Update update) {
        var chatId = update.getMessage().getChatId();
        var text = "";
        Optional<UserTelegram> chatIdOptional = userTelegramService.findByChatId(chatId);
        if (chatIdOptional.isEmpty()) {
            text = "Данный аккаунт Telegram на сайте не зарегистрирован";
            return Optional.of(new SendMessage(chatId.toString(), text));
        }
        try {
            UserTelegram userTelegram = chatIdOptional.get();
            sessionTg.put(chatId.toString(), "userId", Long.toString(userTelegram.getChatId()));
            tgCall.doPost(URL_AUTH_UNNOTIFIED + userTelegram.getUserId()).block();
            text = "Вы отписались от уведомлений";
        } catch (Exception e) {
            log.error("WebClient doPost error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже";
            return Optional.of(new SendMessage(chatId.toString(), text));
        }
        return Optional.of(new SendMessage(chatId.toString(), text));
    }
}