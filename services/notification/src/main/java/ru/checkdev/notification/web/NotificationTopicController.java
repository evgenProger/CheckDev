package ru.checkdev.notification.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.dto.InterviewNotifiDTO;
import ru.checkdev.notification.dto.WisherNotifiDTO;
import ru.checkdev.notification.service.MessagesGenerator;
import ru.checkdev.notification.service.NotificationMessage;
import ru.checkdev.notification.service.UserTelegramService;

import java.util.List;
import java.util.Optional;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 17.11.2023
 */
@Tag(name = "NotificationTopicController", description = "NotificationTopic REST API")
@RestController
@RequestMapping("/notification")
@AllArgsConstructor
public class NotificationTopicController {
    private final UserTelegramService userTelegramService;
    private final NotificationMessage<UserTelegram, String, InnerMessage> notificationMessage;

    /**
     * Метод обрабатывает пост запрос для рассылки уведомлений
     * подписчикам на тему.
     *
     * @param interviewNotifiDTO InterviewNotiDTO
     * @return ResponseEntity<List < InnerMessage>>
     */
    @PostMapping("/topic/")
    public ResponseEntity<List<InnerMessage>> sendMessageSubscribeTopic(@RequestBody InterviewNotifiDTO interviewNotifiDTO) {
        List<UserTelegram> usersTopic = userTelegramService
                .findAllByTopicIdAndUserIdNot(interviewNotifiDTO.getTopicId(),
                        interviewNotifiDTO.getSubmitterId());
        var message = MessagesGenerator.getMessageSubscribeTopic(interviewNotifiDTO);
        List<InnerMessage> result = notificationMessage.sendMessage(usersTopic, message);
        return ResponseEntity.ok(result);
    }

    /**
     * Метод обрабатывает пост запрос для отправки уведомления автору собеседования,
     * о том что добавился участник собеседования.
     *
     * @param wisherNotifiDTO WisherNotifiDTO
     * @return ResponseEntity.
     */
    @PostMapping("/participate/")
    public ResponseEntity<InnerMessage> sendMessageSubmitterInterview(@RequestBody WisherNotifiDTO wisherNotifiDTO) {
        Optional<UserTelegram> userTelegramSubmitter = userTelegramService
                .findByUserId(wisherNotifiDTO.getSubmitterId());
        if (userTelegramSubmitter.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var message = MessagesGenerator.getMessageParticipateWisher(wisherNotifiDTO);
        InnerMessage result = notificationMessage.sendMessage(userTelegramSubmitter.get(), message);
        return ResponseEntity.ok(result);
    }
}
