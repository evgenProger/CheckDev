package ru.checkdev.notification.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.Profile;

import java.util.Calendar;

/**
 * Отправка ботом сообщений в консоль.
 * Для профиля develop
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 08.11.2023
 */
@org.springframework.context.annotation.Profile("develop")
@Service
@Slf4j
public class FakeTgCallConsole implements TgCall {
    @Value("${server.auth}")
    private String urlServiceAuth;

    @Override
    public Mono<Profile> doGet(String url) {
        Profile profile = new Profile("FakeName", "FakeEmail",
                "FakePassword", true, Calendar.getInstance());
        log.info("Fake TgCall doGet method. Request URL: {}{}, Response model: {}", urlServiceAuth, url, profile);
        return Mono.just(profile);
    }

    @Override
    public Mono<Object> doPost(String url, Profile profile) {
        log.info("Fake TgCall doPost method. Request URL: {}{}, model: {}", urlServiceAuth, url, profile);
        return Mono.just(profile);
    }
}
