package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.Optional;

public interface UserTelegramRepository extends JpaRepository<UserTelegram, Integer> {
    Optional<UserTelegram> findByChatId(long chatId);
}
