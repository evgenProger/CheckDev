package ru.checkdev.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class InnerMessageServiceFakeTest {

    @Mock
    private DiscoveryClient discoveryClient;

    private EurekaUriProvider uriProvider;

    @BeforeEach
    void setUp() {
        discoveryClient = Mockito.mock(DiscoveryClient.class);
        uriProvider = new EurekaUriProvider(discoveryClient);
    }

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        var innerMessageRepository = new InnerMessageRepositoryFake();
        var userTelegramRepositoryFake = new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake());
        var userTelegramService = new UserTelegramService(userTelegramRepositoryFake);
        var uriProvider = new EurekaUriProvider(Mockito.mock(DiscoveryClient.class));
        var innerMessageService = new InnerMessageService(
                innerMessageRepository,
                userTelegramService,
                uriProvider
        );
        var botMessage = innerMessageService.saveMessage(
                new InnerMessage(1, 10, "text",
                        new Timestamp(System.currentTimeMillis()), false)
        );
        var result = innerMessageService.findByUserIdAndReadFalse(botMessage.getUserId());
        assertThat(result).contains(botMessage);
    }

    @Test
    public void whenSaveMessagesForSubscribers() throws URISyntaxException {
        var categoryWithTopic = new CategoryWithTopicDTO(1, "Category_1",
                1, "Topic_1", 1, 3);
        var categorySubscribersIds = List.of(1);
        var topicSubscribersIds = List.of(2);
        var service =
                new InnerMessageService(new InnerMessageRepositoryFake(),
                        null, uriProvider);

        ServiceInstance serviceInstance = Mockito.mock(ServiceInstance.class);
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);
        Mockito.when(discoveryClient.getInstances(Mockito.anyString())).thenReturn(serviceInstances);
        Mockito.when(serviceInstance.getUri()).thenReturn(new URI("null"));

        service.saveMessagesForSubscribers(categoryWithTopic, categorySubscribersIds, topicSubscribersIds);
        var categoryMessages = service.findByUserIdAndReadFalse(1);
        var topicMessages = service.findByUserIdAndReadFalse(2);
        assertThat(1).isEqualTo(categoryMessages.size());
        assertThat("В категории Category_1 появилось новое собеседование."
                + System.lineSeparator()
                + "Ссылка на собеседование: null/interview/1")
                .isEqualTo(categoryMessages.get(0).getText());
        assertThat(1).isEqualTo(topicMessages.size());
        assertThat("Появилось новое собеседование по теме Topic_1."
                + System.lineSeparator()
                + "Ссылка на собеседование: null/interview/1")
                .isEqualTo(topicMessages.get(0).getText());
    }
}