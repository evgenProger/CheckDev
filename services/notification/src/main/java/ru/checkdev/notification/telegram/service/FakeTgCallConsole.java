package ru.checkdev.notification.telegram.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.checkdev.notification.domain.Profile;
import ru.checkdev.notification.dto.ProfileTgDTO;

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
    private static final String ERROR_MAIL = "error@exception.er";
    @Value("${server.auth}")
    private String urlServiceAuth;

    @Override
    public Mono<Profile> doGet(String url) {
        Profile profile = new Profile(0, "FakeName", "FakeEmail",
                "FakePassword", true, Calendar.getInstance());
        log.info("Fake TgCall doGet method. Request URL: {}{}, Response model: {}", urlServiceAuth, url, profile);
        return Mono.just(profile);
    }

    @Override
    public Mono<Object> doPost(String url, Profile profile) {
        if (ERROR_MAIL.equals(profile.getEmail())) {
            throw new IllegalArgumentException("Service is error");
        }
        ProfileTgDTO profileTgDTO = new ProfileTgDTO(-23, profile.getUsername(), profile.getEmail());
        log.info("Fake TgCall doPost method. Request URL: {}{}, model: {}", urlServiceAuth, url, profile);
        return Mono.just(profileTgDTO);
    }

    @Override
    public Mono<Object> doPost(String url) {
        ProfileTgDTO profileTgDTO = new ProfileTgDTO(-23, "FakeName", "fake@mail.ru");
        log.info("Fake TgCall doPost method. Request URL: {}{}, model: {}", urlServiceAuth, url);
        return Mono.just(profileTgDTO);
    }
}
