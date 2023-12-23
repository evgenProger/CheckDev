package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.dto.InterviewNotifyDTO;
import ru.checkdev.notification.dto.WisherApprovedDTO;
import ru.checkdev.notification.dto.WisherNotifyDTO;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CheckDev пробное собеседование
 *
 * @author Dmitry Stepanov
 * @version 18.11.2023 00:41
 */
class MessagesGeneratorTest {

    @Test
    void generatorMessageSubscribeTopic() {
        var interviewNotifDTO = InterviewNotifyDTO.of()
                .id(1)
                .title("title")
                .topicId(2)
                .topicName("topic")
                .categoryId(3)
                .categoryName("category")
                .build();
        var expected = String.format(
                "Вы подписаны на тему:%1$s, из категории:%2$s.%3$s"
                        + "По вашей подписке создана новое собеседование.",
                interviewNotifDTO.getTopicName(), interviewNotifDTO.getCategoryName(),
                System.lineSeparator());
        var actual = MessagesGenerator.getMessageSubscribeTopic(interviewNotifDTO);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void generatorMessagepublicParticipateWisher() {
        WisherNotifyDTO wisherNotifyDTO = WisherNotifyDTO.of()
                .interviewId(1)
                .interviewTitle("titleInterview")
                .submitterId(2)
                .userId(3)
                .userName("Вася")
                .contactBy("contact")
                .build();
        String expect = String.format(
                "На ваше собеседование: %s добавился участник: %s%nСсылка на собеседование: null/interview/%s",
                wisherNotifyDTO.getInterviewTitle(),
                wisherNotifyDTO.getUserName(),
                wisherNotifyDTO.getInterviewId());
        String actual = MessagesGenerator.getMessageParticipateWisher(wisherNotifyDTO);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void generatorMessagepublicApprovedWisherWisher() {
        WisherApprovedDTO wisherApprovedDTO = WisherApprovedDTO.of()
                .interviewId(1)
                .wisherId(1)
                .wisherUserId(2)
                .interviewTitle("interview")
                .interviewLink("www")
                .build();
        String expect = String.format(
                "Вы приглашены на собеседование: %s.",
                wisherApprovedDTO.getInterviewTitle()) + System.lineSeparator() + "Ссылка на собеседование: www";
        String actual = MessagesGenerator.getMessageApprovedWisher(wisherApprovedDTO);
        assertThat(actual).isEqualTo(expect);
    }
}