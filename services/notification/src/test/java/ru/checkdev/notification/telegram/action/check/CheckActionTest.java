package ru.checkdev.notification.telegram.action.check;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;
import ru.checkdev.notification.service.EurekaUriProvider;
import ru.checkdev.notification.service.UserTelegramService;
import ru.checkdev.notification.telegram.SessionTg;
import ru.checkdev.notification.telegram.service.FakeTgCallConsole;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CheckActionTest {
    private UserTelegramService userTelegramService = new UserTelegramService(
            new UserTelegramRepositoryFake(
                    new SubscribeTopicRepositoryFake()));

    @Mock
    private DiscoveryClient discoveryClient;

    private EurekaUriProvider uriProvider;

    @BeforeEach
    void setUp() {
        discoveryClient = Mockito.mock(DiscoveryClient.class);
        uriProvider = new EurekaUriProvider(discoveryClient);
    }

    @Test
    void whenNotChatId() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        message.setChat(chat);
        CheckAction checkAction =
                new CheckAction(sessionTg, new FakeTgCallConsole(uriProvider), userTelegramService);
        checkAction.handle(update);
        BotApiMethod<Message> botApiMethod = checkAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Данный аккаунт Telegram на сайте не зарегистрирован";
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenHandleChatIdIsPresentThenReturnMessage() throws URISyntaxException {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        UserTelegram userTelegram = new UserTelegram(0, 1, chat.getId(), false);

        ServiceInstance serviceInstance = Mockito.mock(ServiceInstance.class);
        List<ServiceInstance> serviceInstances = Collections.singletonList(serviceInstance);
        Mockito.when(discoveryClient.getInstances(Mockito.anyString())).thenReturn(serviceInstances);
        Mockito.when(serviceInstance.getUri()).thenReturn(new URI("null"));

        userTelegramService.save(userTelegram);
        message.setChat(chat);
        CheckAction checkAction =
                new CheckAction(sessionTg, new FakeTgCallConsole(uriProvider), userTelegramService);
        BotApiMethod<Message> botApiMethod = checkAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String sl = System.lineSeparator();
        String text = "Имя:" + sl
                + "FakeName" + sl
                + "Email:" + sl
                + "FakeEmail" + sl;
        assertThat(text).isEqualTo(sendMessage.getText());
    }

    @Test
    void whenHandleChatIdIsPresentThenReturnServiceError() {
        Chat chat = new Chat(1L, "type");
        Update update = new Update();
        Message message = new Message();
        update.setMessage(message);
        message.setChat(chat);
        SessionTg sessionTg = new SessionTg();
        UserTelegram userTelegram = new UserTelegram(0, -23, chat.getId(), false);
        userTelegramService.save(userTelegram);
        message.setChat(chat);
        CheckAction checkAction =
                new CheckAction(sessionTg, new FakeTgCallConsole(uriProvider), userTelegramService);
        BotApiMethod<Message> botApiMethod = checkAction.handle(update).get();
        SendMessage sendMessage = (SendMessage) botApiMethod;
        String text = "Сервис не доступен попробуйте позже";
        assertThat(text).isEqualTo(sendMessage.getText());
    }
}