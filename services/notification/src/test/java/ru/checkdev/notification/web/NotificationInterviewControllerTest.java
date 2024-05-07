package ru.checkdev.notification.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 28.11.2023
 */

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class NotificationInterviewControllerTest {

    private final SubscribeTopicRepositoryFake subscribeTopicRepositoryFake =
            new SubscribeTopicRepositoryFake();

    private final UserTelegramRepositoryFake userTelegramRepositoryFake =
            new UserTelegramRepositoryFake(subscribeTopicRepositoryFake);

    private final UserTelegramService userTelegramService =
            new UserTelegramService(userTelegramRepositoryFake);

    private final InnerMessageRepositoryFake innerMessageRepositoryFake =
            new InnerMessageRepositoryFake();

    private final InnerMessageService innerMessageService =
            new InnerMessageService(innerMessageRepositoryFake, userTelegramService,
                    new EurekaUriProvider(Mockito.mock(DiscoveryClient.class)));

    private final NotificationMessageTgFake notificationMessage = new NotificationMessageTgFake();

    @Mock
    private DiscoveryClient discoveryClient;

    private MessagesGenerator messagesGenerator;

    private NotificationInterviewController notifyController;

    @BeforeEach
    void setUp() {
        discoveryClient = Mockito.mock(DiscoveryClient.class);
        EurekaUriProvider uriProvider = new EurekaUriProvider(discoveryClient);
        messagesGenerator = new MessagesGenerator(uriProvider);
        notifyController =
                new NotificationInterviewController(userTelegramService,
                        innerMessageService, notificationMessage, messagesGenerator);
    }

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
        var userTelegram = new UserTelegram(0, 5, 5L, false);
        var userTelegramSubmit = new UserTelegram(0, interviewNotify.getSubmitterId(), interviewNotify.getSubmitterId(), false);
        subscribeTopicRepositoryFake.save(new SubscribeTopic(0, userTelegram.getUserId(), interviewNotify.getTopicId()));
        subscribeTopicRepositoryFake.save(new SubscribeTopic(0, userTelegramSubmit.getUserId(), interviewNotify.getTopicId()));
        userTelegramService.save(userTelegram);
        userTelegramService.save(userTelegramSubmit);
        var messageExpect = messagesGenerator.getMessageSubscribeTopic(interviewNotify);
        var innerMessageExpect = InnerMessage.of()
                .userId(userTelegram.getUserId())
                .text(messageExpect)
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(true)
                .build();
        var expect = ResponseEntity.ok(List.of(innerMessageExpect));
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
        var actual = notifyController.sendMessageSubscribeTopic(interviewNotify);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenSendMessageSubmitterInterviewThenReturnStatusOkBodyInnerMessage() throws URISyntaxException {
        var wisherNotifyDTO = WisherNotifyDTO.of()
                .interviewId(1)
                .interviewTitle("interview1")
                .submitterId(2)
                .userId(3)
                .contactBy("@contact")
                .build();

        ServiceInstance serviceInstance = Mockito.mock(ServiceInstance.class);
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);
        Mockito.when(discoveryClient.getInstances(Mockito.anyString())).thenReturn(serviceInstances);
        Mockito.when(serviceInstance.getUri()).thenReturn(new URI("null"));

        var userTelegramSubmit = new UserTelegram(0, wisherNotifyDTO.getSubmitterId(), wisherNotifyDTO.getSubmitterId(), false);
        userTelegramRepositoryFake.save(userTelegramSubmit);
        var messageExpect = messagesGenerator.getMessageParticipateWisher(wisherNotifyDTO);
        InnerMessage innerMessageExpect = InnerMessage.of()
                .id(1)
                .userId(wisherNotifyDTO.getSubmitterId())
                .text(messageExpect)
                .created(Timestamp.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)))
                .read(false)
                .interviewId(wisherNotifyDTO.getInterviewId())
                .build();
        var expect = ResponseEntity.ok(innerMessageExpect);
        var actual = notifyController.sendMessageSubmitterInterview(wisherNotifyDTO);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenUserNotInTgThenSaveInner() throws URISyntaxException {
        var userTelegramService = new UserTelegramService(userTelegramRepositoryFake);
        var innerMessageRepositoryFake = new InnerMessageRepositoryFake();
        var uriProvider = new EurekaUriProvider(Mockito.mock(DiscoveryClient.class));
        var innerMessageService =
                new InnerMessageService(innerMessageRepositoryFake, userTelegramService, uriProvider);
        var wisherNotifyDTO = WisherNotifyDTO.of()
                .interviewId(1)
                .interviewTitle("interview1")
                .submitterId(2)
                .userId(3)
                .contactBy("@contact")
                .build();

        ServiceInstance serviceInstance = Mockito.mock(ServiceInstance.class);
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);
        Mockito.when(discoveryClient.getInstances(Mockito.anyString())).thenReturn(serviceInstances);
        Mockito.when(serviceInstance.getUri()).thenReturn(new URI("null"));

        var controller = new NotificationInterviewController(userTelegramService, innerMessageService, notificationMessage, messagesGenerator);
        var actual = controller.sendMessageSubmitterInterview(wisherNotifyDTO);
        var msgs = innerMessageService.findByUserIdAndReadFalse(wisherNotifyDTO.getSubmitterId());
        assertThat(msgs.iterator().next()).isEqualTo(actual.getBody());
    }
}