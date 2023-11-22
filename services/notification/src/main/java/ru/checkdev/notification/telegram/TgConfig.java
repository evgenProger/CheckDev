package ru.checkdev.notification.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.checkdev.notification.service.InnerMessageService;
import ru.checkdev.notification.service.UserTelegramService;
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
public class TgConfig {
    private final SessionTg sessionTg = new SessionTg();
    private final TgCall tgCall;
    private final InnerMessageService messageService;
    private final UserTelegramService userTelegramService;
    @Value("${tg.username}")
    private String username;
    @Value("${tg.token}")
    private String token;
    @Value("${server.site.url.login}")
    private String urlLogin;

    public TgConfig(TgCall tgCall,
                    InnerMessageService messageService,
                    UserTelegramService userTelegramService) {
        this.tgCall = tgCall;
        this.messageService = messageService;
        this.userTelegramService = userTelegramService;
    }

    @Bean
    public TgBot initTg() {
        Map<String, List<Action>> actionMap = Map.of(
                "/start", List.of(new InfoAction(List.of(
                        "/start",
                        "/new  зарегистрировать нового пользователя",
                        "/check  получить своё имя и емайл",
                        "/forget  генерация нового пароля",
                        "/notify  подписаться на уведомления",
                        "/unnotify  отписаться от уведомлений"))),
                "/new", List.of(
                        new RegAction10(userTelegramService),
                        new RegAction11(sessionTg),
                        new RegAction15(userTelegramService),
                        new RegAction16(sessionTg),
                        new RegAction20(sessionTg),
                        new RegAction30(sessionTg, tgCall, userTelegramService, urlLogin),
                        new SaveInnerMessageAction(sessionTg, messageService)
                ),
                "/check", List.of(
                        new CheckAction(sessionTg, tgCall, userTelegramService),
                        new SaveInnerMessageAction(sessionTg, messageService)),
                "/forget", List.of(
                        new ForgetAction(sessionTg, tgCall, userTelegramService),
                        new SaveInnerMessageAction(sessionTg, messageService)),
                "/notify", List.of(
                        new NotifyAction(sessionTg, tgCall, userTelegramService),
                        new SaveInnerMessageAction(sessionTg, messageService)),
                "/unnotify", List.of(
                        new UnNotifyAction(sessionTg, tgCall, userTelegramService),
                        new SaveInnerMessageAction(sessionTg, messageService))
        );
        TgBot menu = new TgBot(actionMap, username, token);
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(menu);
        return menu;
    }
}