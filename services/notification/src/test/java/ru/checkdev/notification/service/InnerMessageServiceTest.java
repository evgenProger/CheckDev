package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.CategoryWithTopicDTO;
import ru.checkdev.notification.telegram.TgRun;
import ru.checkdev.notification.telegram.service.TgAuthCallWebClint;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
public class InnerMessageServiceTest {
    @Autowired
    private InnerMessageService service;

    @MockBean
    private TgRun tgRun;

    @MockBean
    private TgAuthCallWebClint tgAuthCallWebClint;

    @Test
    public void whenSaveBotMessageAndGetTheSame() {
        InnerMessage botMessage = this.service.saveMessage(new InnerMessage(1, 10, "text",
                new Timestamp(System.currentTimeMillis()), false));
        List<InnerMessage> result = this.service.findByUserIdAndReadFalse(10);
        assertThat(result).contains(botMessage);
    }

    @Test
    public void whenSaveMessagesForSubscribers() {
        var categoryWithTopic = new CategoryWithTopicDTO(1, "Category_1",
                1, "Topic_1");
        var categorySubscribersIds = List.of(1);
        var topicSubscribersIds = List.of(2);
        service.saveMessagesForSubscribers(categoryWithTopic, categorySubscribersIds, topicSubscribersIds);
        var categoryMessages = service.findByUserIdAndReadFalse(1);
        var topicMessages = service.findByUserIdAndReadFalse(2);
        assertEquals(1, categoryMessages.size());
        assertEquals("В категории \"Category_1\" появилось новое собеседование.",
                categoryMessages.get(0).getText());
        assertEquals(1, topicMessages.size());
        assertEquals("Появилось новое собеседование по теме Topic_1.",
                topicMessages.get(0).getText());
    }
}