package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.service.TgCall;

import java.sql.Timestamp;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class UnNotifyAction implements Action {
    private static final String URL_AUTH_UNNOTIFIED = "/profiles/tg/unnotified/";
    private final TgCall tgCall;
    private final UserTelegramService userTelegramService;
    private final InnerMessageService messageService;

    @Override
    public BotApiMethod<Message> handle(Message message) {
        var chatId = message.getChatId();
        var text = "";
        Optional<UserTelegram> chatIdOptional = userTelegramService.findByChatId(chatId);
        if (chatIdOptional.isEmpty()) {
            text = "Данный аккаунт Telegram на сайте не зарегистрирован";
            return new SendMessage(chatId.toString(), text);
        }
        try {
            UserTelegram userTelegram = chatIdOptional.get();
            tgCall.doPost(URL_AUTH_UNNOTIFIED + userTelegram.getUserId()).block();
            text = "Вы отписались от уведомлений";
            InnerMessage innerMessage = new InnerMessage();
            innerMessage.setUserId(userTelegram.getUserId());
            innerMessage.setText(text);
            innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
            messageService.saveMessage(innerMessage);
            userTelegramService.delete(chatIdOptional.get().getId());
        } catch (Exception e) {
            log.error("WebClient doPost error: {}", e.getMessage());
            text = "Сервис не доступен попробуйте позже";
            return new SendMessage(chatId.toString(), text);
        }
        return new SendMessage(chatId.toString(), text);
    }

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}