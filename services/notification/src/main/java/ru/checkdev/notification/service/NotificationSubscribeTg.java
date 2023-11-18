package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.telegram.TgBot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Метод отправляет сообщение все UserTelegram
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 17.11.2023
 */
@Service
@AllArgsConstructor
@Slf4j
public class NotificationSubscribeTg implements NotificationSubscribe<UserTelegram, String> {
    private final TgBot tgBot;
    private final InnerMessageService innerMessageService;

    /**
     * Метод отправляет сообщения пользователям в телеграмм
     *
     * @param targets List<UserTelegram>
     * @param value String message
     * @return List<InnerMessage>
     */
    @Override
    public List<InnerMessage> sendMessage(List<UserTelegram> targets, String value) {
        List<InnerMessage> innerMessages = new ArrayList<>();
        for (UserTelegram user : targets) {
            var messageTg = new SendMessage(String.valueOf(user.getChatId()), value);
            var innerMessage = new InnerMessage(0, user.getUserId(), value, new Timestamp(System.currentTimeMillis()), true);
            try {
                tgBot.execute(messageTg);
            } catch (Exception e) {
                log.error("Send message by UserID:{}, from telegram Error:{}", user.getUserId(), e);
                innerMessage.setRead(false);
            }
            innerMessageService.saveMessage(innerMessage);
            innerMessages.add(innerMessage);
        }
        return innerMessages;
    }
}
