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
import ru.checkdev.notification.dto.WisherApprovedDTO;
import ru.checkdev.notification.service.MessagesGenerator;
import ru.checkdev.notification.service.NotificationMessage;
import ru.checkdev.notification.service.UserTelegramService;

import java.util.Optional;


@Tag(name = "NotificationApprovedWisherController", description = "NotificationApprovedWisher REST API")
@RestController
@RequestMapping("/notificationWisher")
@AllArgsConstructor
public class NotificationWisherController {

    private final UserTelegramService userTelegramService;
    private final NotificationMessage<UserTelegram, String, InnerMessage> notificationMessage;

    @PostMapping("/approvedWisher/")
    public ResponseEntity<InnerMessage> sendMessageApprovedWisher(
            @RequestBody WisherApprovedDTO wisherApprovedNotifyDTO) {
        Optional<UserTelegram> userTelegramSubmitter = userTelegramService
                .findByUserId(wisherApprovedNotifyDTO.getWisherUserId());
        if (userTelegramSubmitter.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var message = MessagesGenerator.getMessageApprovedWisher(wisherApprovedNotifyDTO);
        InnerMessage result = notificationMessage.sendMessage(userTelegramSubmitter.get(), message);
        return ResponseEntity.ok(result);
    }
}
