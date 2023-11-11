package ru.checkdev.notification.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.checkdev.notification.service.ChatIdService;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.telegram.action.*;
import ru.checkdev.notification.telegram.service.TgCall;

import java.util.List;
import java.util.Map;

/**
 * Инициализация телеграм бот,
 * username = берем из properties
 * token = берем из properties
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.09.2023
 */
@Component
@Slf4j
public class TgRun {
    private final TgCall tgCall;
    private final InnerMessageService messageService;
    private final ChatIdService chatIdService;
    @Value("${tg.username}")
    private String username;
    @Value("${tg.token}")
    private String token;
    @Value("${server.site.url.login}")
    private String urlLogin;

    public TgRun(TgCall tgCall, InnerMessageService messageService, ChatIdService chatIdService) {
        this.tgCall = tgCall;
        this.messageService = messageService;
        this.chatIdService = chatIdService;
    }

    @Bean
    public void initTg() {
        Map<String, Action> actionMap = Map.of(
                "/start", new InfoAction(List.of(
                        "/start",
                        "/new  зарегистрировать нового пользователя",
                        "/check  получить своё имя и емайл",
                        "/forget  генерация нового пароля",
                        "/notify  подписаться на уведомления",
                        "/unnotify  отписаться от уведомлений")),
                "/new", new RegAction(tgCall, chatIdService, messageService, urlLogin),
                "/check", new CheckAction(tgCall, chatIdService, messageService),
                "/forget", new ForgetAction(tgCall, chatIdService, messageService),
                "/notify", new NotifyAction(tgCall, chatIdService, messageService),
                "/unnotify", new UnNotifyAction(tgCall, chatIdService, messageService)

        );

        try {
            BotMenu menu = new BotMenu(actionMap, username, token);
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(menu);
        } catch (TelegramApiException e) {
            log.error("Telegram bot: {}, ERROR {}", username, e.getMessage());
        }
    }
}