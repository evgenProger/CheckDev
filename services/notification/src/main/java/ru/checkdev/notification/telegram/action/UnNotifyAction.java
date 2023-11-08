package ru.checkdev.notification.telegram.action;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.checkdev.notification.domain.ChatId;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.Profile;
import ru.checkdev.notification.service.ChatIdService;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.telegram.config.TgConfig;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;

import java.sql.Timestamp;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class UnNotifyAction implements Action {
    private static final String ERROR_OBJECT = "error";
    private static final String URL_AUTH_UNNOTIFIED = "/person/unnotified";
    private final TgAuthCallWebClint authCallWebClint;
    private final ChatIdService chatIdService;
    private final InnerMessageService messageService;
    private final TgConfig tgConfig = new TgConfig("tg/", 8);

    @Override
    public BotApiMethod<Message> handle(Message message) {


    Object result;
    var chatIdString = message.getChatId().toString();
    var text = "";
    Optional<ChatId> chatIdOptional = chatIdService.findByChatId(chatIdString);
        if (chatIdOptional.isEmpty()) {
        text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        return new SendMessage(chatIdString, text);
    }
        try {
        ChatId chatId = chatIdOptional.get();
        Profile profile = new Profile();
        profile.setEmail(chatId.getEmail());
        result = authCallWebClint.doPost(URL_AUTH_UNNOTIFIED, profile).block();
        text = "Вы отписались от уведомлений";
        InnerMessage innerMessage = new InnerMessage();
        innerMessage.setChatId(chatId.getId());
        innerMessage.setText(text);
        innerMessage.setCreated(new Timestamp(System.currentTimeMillis()));
        messageService.saveMessage(innerMessage);
    } catch (Exception e) {
        log.error("WebClient doPost error: {}", e.getMessage());
        text = "Сервис не доступен попробуйте позже";
        return new SendMessage(chatIdString, text);
    }

    var mapObject = tgConfig.getObjectToMap(result);

        if (mapObject.containsKey(ERROR_OBJECT)) {
        text = "Ошибка: " + mapObject.get(ERROR_OBJECT);
        return new SendMessage(chatIdString, text);
    }

        return new SendMessage(chatIdString, text);
}

    @Override
    public BotApiMethod<Message> callback(Message message) {
        return handle(message);
    }
}