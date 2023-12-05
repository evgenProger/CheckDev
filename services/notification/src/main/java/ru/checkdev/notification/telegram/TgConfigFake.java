package ru.checkdev.notification.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.action.Action;
import ru.checkdev.notification.telegram.action.check.CheckAction;
import ru.checkdev.notification.telegram.action.info.InfoAction;
import ru.checkdev.notification.telegram.action.notify.NotifyAction;
import ru.checkdev.notification.telegram.action.notify.UnNotifyAction;
import ru.checkdev.notification.telegram.action.reg.*;
import ru.checkdev.notification.telegram.service.TgCall;

import java.util.List;
import java.util.Map;

/**
 * Класс создание экземпляр класса TgBotFake для профиля develop без использованием Telegram API
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 04.12.2023
 */
@Component
@Profile("develop")
@Slf4j
public class TgConfigFake {
    private final SessionTg sessionTg = new SessionTg();
    private final TgCall tgCall;
    private final UserTelegramService userTelegramService;
    @Value("${tg.username}")
    private String username;
    @Value("${tg.token}")
    private String token;
    @Value("${server.site.url.login}")
    private String urlLogin;

    public TgConfigFake(TgCall tgCall,
                        UserTelegramService userTelegramService) {
        this.tgCall = tgCall;
        this.userTelegramService = userTelegramService;
    }

    @Bean
    public Bot initTg() {
        Map<String, List<Action>> actionMap = Map.of(
                "/start", List.of(new InfoAction(List.of(
                        "/start",
                        "/new  зарегистрировать нового пользователя",
                        "/check  получить своё имя и Email",
                        "/notify  подписаться на уведомления",
                        "/unnotify  отписаться от уведомлений"))),
                "/new", List.of(
                        new RegAskNameAction(userTelegramService),
                        new RegPutNameAction(sessionTg),
                        new RegAskEmailAction(userTelegramService),
                        new RegPutEmailAction(sessionTg),
                        new RegCheckEmailAction(sessionTg),
                        new RegSaveUserAction(sessionTg, tgCall, userTelegramService, urlLogin)
                ),
                "/check", List.of(new CheckAction(sessionTg, tgCall, userTelegramService)),
                "/notify", List.of(new NotifyAction(sessionTg, tgCall, userTelegramService)),
                "/unnotify", List.of(new UnNotifyAction(sessionTg, tgCall, userTelegramService))
        );
        TgBootFake menu = new TgBootFake(actionMap, username, token);
        return menu;
    }
}
