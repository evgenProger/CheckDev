package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.repository.UserTelegramRepository;
import ru.checkdev.notification.telegram.service.Notificator;

import java.util.List;

@Service
@AllArgsConstructor
public class NotificationMessagesService {

    private final UserTelegramRepository userTelegramRepository;
    private final Notificator notificator;

    public void sendMessagesToCategorySubscribers(List<Integer> categorySubscribersIds,
                                                  CategoryWithTopicDTO categoryWithTopicDTO) {
        userTelegramRepository.findChatIdInUserIds(categorySubscribersIds)
                .forEach(chatId ->
                        sendNotificationToCategorySubscriber(chatId,
                                categoryWithTopicDTO.getCategoryName()));
    }

    public String sendNotificationToCategorySubscriber(long chatId, String categoryName) {
        return notificator.sendMessage(chatId,
                String.format("В категории \"%s\" появилось новое собеседование.", categoryName));
    }
}
