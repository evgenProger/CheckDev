package ru.checkdev.notification.web;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.repository.InnerMessageRepositoryFake;
import ru.checkdev.notification.service.InnerMessageService;
import static org.assertj.core.api.Assertions.assertThat;


public class InnerMessageControllerFakeTest {

    @Test
    public void whenFindBotMessageByUserId() {
        var botMessage = new InnerMessage(1, 10,
                "text", null, false);
        var innerMessageService = new InnerMessageService(new InnerMessageRepositoryFake(),
                null, null);
        var savedMsg = innerMessageService.saveMessage(botMessage);
        var controller = new InnerMessageController(
                innerMessageService, null, null, null
        );
        var resp = controller.findMessage(savedMsg.getUserId());
        assertThat(resp.getBody()).containsOnly(savedMsg);
    }
}