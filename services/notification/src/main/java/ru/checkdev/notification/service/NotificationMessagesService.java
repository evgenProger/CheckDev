package ru.checkdev.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.dto.FeedbackNotificationDTO;
import ru.checkdev.notification.dto.WisherApprovedDTO;
import ru.checkdev.notification.repository.UserTelegramRepository;
import ru.checkdev.notification.telegram.Bot;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class NotificationMessagesService {

    private final UserTelegramRepository userTelegramRepository;
    private final InnerMessageService innerMessageService;
    private final Bot bot;
    private final MessagesGenerator messagesGenerator;
    @Value("${service.urlSite}")
    private String urlSite;

    public NotificationMessagesService(
            UserTelegramRepository userTelegramRepository,
            InnerMessageService innerMessageService,
            Bot bot,
            MessagesGenerator messagesGenerator
    ) {
        this.userTelegramRepository = userTelegramRepository;
        this.innerMessageService = innerMessageService;
        this.bot = bot;
        this.messagesGenerator = messagesGenerator;
    }

    public void sendMessagesToCategorySubscribers(List<Integer> categorySubscribersIds,
                                                  CategoryWithTopicDTO categoryWithTopicDTO) {
        userTelegramRepository.findChatIdInUserIds(categorySubscribersIds)
                .forEach(chatId ->
                        sendNotificationToCategorySubscriber(chatId,
                                categoryWithTopicDTO));
    }

    public void sendNotificationToCategorySubscriber(long chatId, CategoryWithTopicDTO categoryWithTopicDTO) {
        bot.send(new SendMessage(
                        String.valueOf(chatId),
                        "В категории "
                                + categoryWithTopicDTO.getCategoryName()
                                + " появилось новое собеседование."
                                + System.lineSeparator()
                                + "Ссылка на собеседование: "
                                + urlSite
                                + "/interview/" + categoryWithTopicDTO.getInterviewId()
                )
        );
    }

    public void sendFeedbackNotification(FeedbackNotificationDTO feedbackNotification) {
        var optionalChatId = userTelegramRepository
                .findChatIdByUserId(feedbackNotification.getRecipientId());
        var message = "Пользователь "
                + feedbackNotification.getSenderName()
                + " оставил Вам отзыв о собеседовании на тему "
                + feedbackNotification.getInterviewName()
                + System.lineSeparator()
                + "Ссылка на собеседование: "
                + urlSite
                + "/interview/" + feedbackNotification.getInterviewId();
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

    public void sendApprovedNotification(WisherApprovedDTO wisherApprovedDTO) {
        var optionalChatId = userTelegramRepository
                .findChatIdByUserId(wisherApprovedDTO.getWisherUserId());
        var message = String.format("Вы приглашены на собеседование \"[%s](%s)\".%sСвяжитесь с автором: %s",
                wisherApprovedDTO.getInterviewTitle(),
                wisherApprovedDTO.getInterviewLink(),
                System.lineSeparator(),
                wisherApprovedDTO.getContactBy());
        InnerMessage innerMessage = InnerMessage.of()
                .userId(wisherApprovedDTO.getWisherUserId())
                .text(messagesGenerator.getMessageApprovedWisher(wisherApprovedDTO))
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(false)
                .interviewId(wisherApprovedDTO.getInterviewId())
                .build();
        CompletableFuture.supplyAsync(() -> innerMessageService.saveMessage(innerMessage));
        if (optionalChatId.isPresent()) {
            var chatId = optionalChatId.get();
            var sendNotification = new SendMessage(String.valueOf(chatId), message);
            sendNotification.setParseMode("Markdown");
            bot.send(sendNotification);
        }
    }
}
