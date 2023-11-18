package ru.checkdev.notification.repository;

import ru.checkdev.notification.domain.SubscribeTopic;
import java.util.List;

public class SubscribeTopicRepositoryFake
        extends CrudRepositoryFake<SubscribeTopic>
        implements SubscribeTopicRepository {

    @Override
    public List<SubscribeTopic> findAll() {
        return memory.values()
                .stream()
                .toList();
    }

    @Override
    public List<SubscribeTopic> findByUserId(int id) {
        return memory.values().stream()
                .filter(subscribeTopic -> subscribeTopic.getUserId() == id)
                .toList();
    }

    @Override
    public SubscribeTopic findByUserIdAndTopicId(int userId, int topicId) {
        return memory.values().stream()
                .filter(subscribeTopic -> subscribeTopic.getUserId() == userId)
                .filter(subscribeTopic -> subscribeTopic.getTopicId() == topicId)
                .findFirst().orElse(null);
    }

    @Override
    public List<Integer> findUserIdByTopicIdExcludeCurrent(int topicId, int excludedUserId) {
        return memory.values().stream()
                .filter(subscribeTopic -> subscribeTopic.getTopicId() == topicId
                        && subscribeTopic.getUserId() != excludedUserId)
                .map(SubscribeTopic::getUserId)
                .toList();
    }
}
