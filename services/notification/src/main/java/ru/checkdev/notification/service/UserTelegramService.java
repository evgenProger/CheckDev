package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.UserTelegramRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserTelegramService {
    private final UserTelegramRepository repository;

    public boolean save(UserTelegram userTelegram) {
        try {
            repository.save(userTelegram);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    /**
     * Метод возвращает Optional<UserTelegram> по chatId
     *
     * @param chatId UserTelegram chatId long
     * @return Optional<UserTelegram>
     */
    public Optional<UserTelegram> findByChatId(long chatId) {
        return repository.findByChatId(chatId);
    }

    public Optional<UserTelegram> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    /**
     * Метод возвращает всех подписчиков кроме автора.
     *
     * @param topicId SubscribeTopic TopicId
     * @param userId  NOT SubscribeTopic UserId
     * @return List<SubscribeTopic>
     */
    public List<UserTelegram> findAllByTopicIdAndUserIdNot(int topicId, int userId) {
        return repository.findAllByTopicIdAndUserIdNot(topicId, userId);
    }
}