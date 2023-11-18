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
import ru.checkdev.notification.dto.InterviewNotifDTO;
import ru.checkdev.notification.service.MessagesGenerator;
import ru.checkdev.notification.service.NotificationSubscribe;
import ru.checkdev.notification.service.UserTelegramService;

import java.util.List;

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
    private final NotificationSubscribe<UserTelegram, String> notificationSubscribe;

    /**
     * Метод обрабатывает пост запрос для рассылки уведомлений
     *
     * @param interviewNotifDTO
     * @return ResponseEntity<List < InnerMessage>
     */
    @PostMapping("/topic/")
    public ResponseEntity<List<InnerMessage>> sendMessageSubscribeTopic(@RequestBody InterviewNotifDTO interviewNotifDTO) {
        List<UserTelegram> usersTopic = userTelegramService
                .findAllByTopicIdAndUserIdNot(interviewNotifDTO.getTopicId(),
                        interviewNotifDTO.getSubmitterId());
        var message = MessagesGenerator.generatorMessageSubscribeTopic(interviewNotifDTO);
        List<InnerMessage> result = notificationSubscribe.sendMessage(usersTopic, message);
        return ResponseEntity.ok(result);
    }
}
