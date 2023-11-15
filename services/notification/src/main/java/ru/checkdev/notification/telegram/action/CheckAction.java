package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.Profile;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.telegram.service.TgCall;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * Telegram Action команда /check
 * Получить сове имя и email
 *
 */
@AllArgsConstructor
@Slf4j
public class CheckAction implements Action {
    private static final String URL_AUTH_CURRENT = "/profiles/tg/";
    private final TgCall tgCall;
    private final UserTelegramService userTelegramService;
    private final InnerMessageService messageService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatIdString = message.getChatId();
        var out = new StringBuilder();
        String sl = System.lineSeparator();
        Optional<UserTelegram> chatIdOptional = userTelegramService.findByChatId(chatIdString);
        if (chatIdOptional.isEmpty()) {
            out.append("Данный аккаунт Telegram на сайте не зарегистрирован").append(sl);
            return new SendMessage(chatIdString.toString(), out.toString());
        }
        try {
            UserTelegram userTelegram = chatIdOptional.get();
            Profile profile = tgCall
                    .doGet(URL_AUTH_CURRENT + userTelegram.getUserId()).block();
            out.append("Имя:")
                    .append(sl)
                    .append(profile.getUsername())
                    .append(sl)
                    .append("Email:")
                    .append(sl)
                    .append(profile.getEmail())
                    .append(sl);
            InnerMessage innerMessage = new InnerMessage();
            innerMessage.setUserId(profile.getId());
            innerMessage.setText(out.toString());
            innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
            messageService.saveMessage(innerMessage);
            return new SendMessage(chatIdString.toString(), out.toString());
        } catch (Exception e) {
            log.error("WebClient doPost error: {}", e.getMessage());
            out.append("Сервис не доступен попробуйте позже").append(sl);
            return new SendMessage(chatIdString.toString(), out.toString());
        }
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}