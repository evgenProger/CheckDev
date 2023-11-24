package ru.checkdev.notification.repository;

import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserTelegramRepositoryFake
        extends CrudRepositoryFake<UserTelegram>
        implements UserTelegramRepository {

    private final SubscribeTopicRepositoryFake subscribeTopics;

    public UserTelegramRepositoryFake(SubscribeTopicRepositoryFake subscribeTopics) {
        this.subscribeTopics = subscribeTopics;
    }

    @Override
    public Optional<UserTelegram> findByChatId(long chatId) {
        return memory.values().stream()
                .filter(userTelegram -> userTelegram.getChatId() == chatId)
                .findFirst();
    }

    @Override
    public List<Long> findChatIdInUserIds(List<Integer> userIds) {
        return memory.values().stream()
                .filter(userTelegram -> userIds.contains(userTelegram.getUserId()))
                .map(UserTelegram::getChatId)
                .toList();
    }

    @Override
    public Optional<Long> findChatIdByUserId(int userId) {
        return memory.values().stream()
                .filter(u -> u.getUserId() == userId)
                .map(u -> Optional.of(u.getChatId())).findFirst().orElse(Optional.empty());
    }

    @Override
    public Optional<UserTelegram> findByUserId(int userId) {
        return memory.values().stream()
                .filter(userTelegram -> userTelegram.getUserId() == userId)
                .findFirst();
    }

    @Override
    public List<UserTelegram> findAllByTopicIdAndUserIdNot(int topicId, int userId) {
        List<SubscribeTopic> subscribeUserByTopic = subscribeTopics.findAll();
        List<UserTelegram> result = new ArrayList<>();
        for (UserTelegram user : memory.values()) {
            for (SubscribeTopic subscribeTopic : subscribeUserByTopic) {
                if (subscribeTopic.getTopicId() == topicId
                        && subscribeTopic.getUserId() == user.getUserId()
                        && user.getUserId() != userId) {
                    result.add(user);
                    break;
                }
            }
        }
        return result;
    }
}
