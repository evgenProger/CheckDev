package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.dto.InterviewNotifDTO;

/**
 * CheckDev пробное собеседование
 *
 * @author Dmitry Stepanov
 * @version 18.11.2023 00:41
 */
class MessagesGeneratorTest {

    @Test
    void generatorMessageSubscribeTopic() {
        var interviewNotifDTO = InterviewNotifDTO.of()
                .id(1)
                .title("title")
                .topicId(2)
                .topicName("topic")
                .categoryId(3)
                .categoryName("category")
                .build();
        var expected = String.format(
                "Вы подписаны на тему:%1$s, из категории:%2$s.%3$s"
                        + "По вашей подписки создана новое собеседование.",
                interviewNotifDTO.getTopicName(), interviewNotifDTO.getCategoryName(),
                System.lineSeparator());
        var actual = MessagesGenerator.generatorMessageSubscribeTopic(interviewNotifDTO);
    }
}