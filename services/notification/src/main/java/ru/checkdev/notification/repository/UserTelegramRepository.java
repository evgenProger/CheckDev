package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.List;
import java.util.Optional;

public interface UserTelegramRepository extends JpaRepository<UserTelegram, Integer> {
    Optional<UserTelegram> findByChatId(long chatId);

    @Query("SELECT ut.chatId FROM cd_user_telegram ut WHERE ut.userId IN :userIds")
    List<Long> findChatIdInUserIds(@Param("userIds") List<Integer> userIds);

    Optional<UserTelegram> findByUserId(int userId);
}
