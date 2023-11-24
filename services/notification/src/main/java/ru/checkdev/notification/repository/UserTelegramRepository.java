package ru.checkdev.notification.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.List;
import java.util.Optional;

public interface UserTelegramRepository extends CrudRepository<UserTelegram, Integer> {
    Optional<UserTelegram> findByChatId(long chatId);

    @Query("SELECT ut.chatId FROM cd_user_telegram ut WHERE ut.userId IN :userIds")
    List<Long> findChatIdInUserIds(@Param("userIds") List<Integer> userIds);

    @Query("SELECT ut.chatId FROM cd_user_telegram ut WHERE ut.userId = :userId")
    Optional<Long> findChatIdByUserId(@Param("userId") int userId);

    Optional<UserTelegram> findByUserId(int userId);

    /**
     * Метод возвращает всех подписчиков кроме автора.
     *
     * @param topicId SubscribeTopic TopicId
     * @param userId  NOT SubscribeTopic UserId
     * @return List<SubscribeTopic>
     */
    @Query("""
            SELECT new ru.checkdev.notification.domain.UserTelegram(ut.id, ut.userId, ut.chatId)
            FROM cd_user_telegram ut 
            JOIN cd_subscribe_topic st 
            ON ut.userId !=:userId AND ut.userId = st.userId AND st.topicId =:topicId 
            """)
    List<UserTelegram> findAllByTopicIdAndUserIdNot(@Param("topicId") int topicId, @Param("userId") int userId);
}
