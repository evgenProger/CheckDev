package ru.checkdev.notification.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.checkdev.notification.telegram.BotMenu;

/**
 * Использование бота для отправки сообщений слоем Service
 * Arcady555 16.11.2023,
 */
@Service
@Slf4j
public class TgRunForService {
    @Value("${tg.username}")
    private String username;
    @Value("${tg.token}")
    private String token;

    public void send(String chatId, String text) {
        try {
            BotMenu menu = new BotMenu(username, token);
            menu.execute(new SendMessage(chatId, text));
        } catch (TelegramApiException e) {
            log.error("Telegram bot: {}, ERROR {}", username, e.getMessage());
        }
    }
}