package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.dto.FeedbackNotificationDTO;
import ru.checkdev.notification.repository.UserTelegramRepository;
import ru.checkdev.notification.telegram.TgBot;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class NotificationMessagesService {

    private final UserTelegramRepository userTelegramRepository;
    private final InnerMessageService innerMessageService;
    private final TgBot bot;

    public void sendMessagesToCategorySubscribers(List<Integer> categorySubscribersIds,
                                                  CategoryWithTopicDTO categoryWithTopicDTO) {
        userTelegramRepository.findChatIdInUserIds(categorySubscribersIds)
                .forEach(chatId ->
                        sendNotificationToCategorySubscriber(chatId,
                                categoryWithTopicDTO.getCategoryName()));
    }

    public void sendNotificationToCategorySubscriber(long chatId, String categoryName) {
        bot.send(new SendMessage(String.valueOf(chatId),
                String.format("В категории \"%s\" появилось новое собеседование.", categoryName)));
    }

    public void sendFeedbackNotification(FeedbackNotificationDTO feedbackNotification) {
        var optionalChatId = userTelegramRepository
                .findChatIdByUserId(feedbackNotification.getRecipientId());
        var message = String.format("Пользователь %s оставил Вам отзыв о собеседовании на тему \"%s\"",
                feedbackNotification.getSenderName(),
                feedbackNotification.getInterviewName());
        optionalChatId.ifPresent(aLong -> bot.send(new SendMessage(String.valueOf(aLong), message)));
        InnerMessage innerMessage = InnerMessage.of()
                .userId(feedbackNotification.getRecipientId())
                .text(message)
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(false)
                .interviewId(feedbackNotification.getInterviewId())
                .build();
        innerMessageService.saveMessage(innerMessage);
    }
}
