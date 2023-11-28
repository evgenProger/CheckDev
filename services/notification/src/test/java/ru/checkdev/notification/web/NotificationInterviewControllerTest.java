package ru.checkdev.notification.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.dto.InterviewNotifyDTO;
import ru.checkdev.notification.dto.WisherNotifyDTO;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.*;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 28.11.2023
 */
class NotificationInterviewControllerTest {
    private final SubscribeTopicRepositoryFake subscribeTopicRepositoryFake = new SubscribeTopicRepositoryFake();
    private final UserTelegramRepositoryFake userTelegramRepositoryFake = new UserTelegramRepositoryFake(subscribeTopicRepositoryFake);
    private final UserTelegramService userTelegramService = new UserTelegramService(userTelegramRepositoryFake);
    private final InnerMessageRepositoryFake innerMessageRepositoryFake = new InnerMessageRepositoryFake();
    private final InnerMessageService innerMessageService = new InnerMessageService(innerMessageRepositoryFake, userTelegramService);
    private final NotificationMessageTgFake notificationMessage = new NotificationMessageTgFake();

    @Test
    void whenSendMessageSubscribeTopicThenReturnStatusOkBodyListOneMessage() {
        var interviewNotify = InterviewNotifyDTO.of()
                .id(1)
                .submitterId(2)
                .title("interview1")
                .topicId(2)
                .topicName("topic2")
                .categoryId(2)
                .categoryName("category2")
                .build();
        var userTelegram = new UserTelegram(0, 5, 5L);
        var userTelegramSubmit = new UserTelegram(0, interviewNotify.getSubmitterId(), interviewNotify.getSubmitterId());
        subscribeTopicRepositoryFake.save(new SubscribeTopic(0, userTelegram.getUserId(), interviewNotify.getTopicId()));
        subscribeTopicRepositoryFake.save(new SubscribeTopic(0, userTelegramSubmit.getUserId(), interviewNotify.getTopicId()));
        userTelegramService.save(userTelegram);
        userTelegramService.save(userTelegramSubmit);
        var messageExpect = MessagesGenerator.getMessageSubscribeTopic(interviewNotify);
        var innerMessageExpect = InnerMessage.of()
                .userId(userTelegram.getUserId())
                .text(messageExpect)
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(true)
                .build();
        var expect = ResponseEntity.ok(List.of(innerMessageExpect));
        var notifyController = new NotificationInterviewController(userTelegramService, innerMessageService, notificationMessage);
        var actual = notifyController.sendMessageSubscribeTopic(interviewNotify);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenSendMessageSubscribeTopicThenReturnStatusOkBodyListEmpty() {
        var interviewNotify = InterviewNotifyDTO.of()
                .id(1)
                .submitterId(2)
                .title("interview1")
                .topicId(2)
                .topicName("topic2")
                .categoryId(2)
                .categoryName("category2")
                .build();
        var expect = ResponseEntity.ok(emptyList());
        var notifyController = new NotificationInterviewController(userTelegramService, innerMessageService, notificationMessage);
        var actual = notifyController.sendMessageSubscribeTopic(interviewNotify);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenSendMessageSubmitterInterviewThenReturnStatusOkBodyInnerMessage() {
        var wisherNotifyDTO = WisherNotifyDTO.of()
                .interviewId(1)
                .interviewTitle("interview1")
                .submitterId(2)
                .userId(3)
                .contactBy("@contact")
                .build();
        var userTelegramSubmit = new UserTelegram(0, wisherNotifyDTO.getSubmitterId(), wisherNotifyDTO.getSubmitterId());
        userTelegramRepositoryFake.save(userTelegramSubmit);
        var messageExpect = MessagesGenerator.getMessageParticipateWisher(wisherNotifyDTO);
        InnerMessage innerMessageExpect = InnerMessage.of()
                .id(1)
                .userId(wisherNotifyDTO.getSubmitterId())
                .text(messageExpect)
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(false)
                .interviewId(wisherNotifyDTO.getInterviewId())
                .build();
        var expect = ResponseEntity.ok(innerMessageExpect);
        var controller = new NotificationInterviewController(userTelegramService, innerMessageService, notificationMessage);
        var actual = controller.sendMessageSubmitterInterview(wisherNotifyDTO);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenSendMessageSubmitterInterviewThenReturnStatusNotFound() {
        var wisherNotifyDTO = WisherNotifyDTO.of()
                .interviewId(1)
                .interviewTitle("interview1")
                .submitterId(2)
                .userId(3)
                .contactBy("@contact")
                .build();
        var expect = ResponseEntity.notFound().build();
        var controller = new NotificationInterviewController(userTelegramService, innerMessageService, notificationMessage);
        var actual = controller.sendMessageSubmitterInterview(wisherNotifyDTO);
        assertThat(actual).isEqualTo(expect);
    }
}