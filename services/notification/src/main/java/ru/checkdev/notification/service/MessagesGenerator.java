package ru.checkdev.notification.service;

import org.springframework.stereotype.Component;
import ru.checkdev.notification.dto.InterviewNotifyDTO;
import ru.checkdev.notification.dto.WisherApprovedDTO;
import ru.checkdev.notification.dto.WisherNotifyDTO;

/**
 * CheckDev пробное собеседование
 * Класс предназначен для генерации сообщения для рассылки.
 *
 * @author Dmitry Stepanov
 * @version 17.11.2023 23:12
 */
@Component
public class MessagesGenerator {
    /**
     * Генерация сообщения для отправки при подписке на тему.
     *
     * @param interviewNotifyDTO InterviewNotifDTO
     * @return String
     */
    public static String getMessageSubscribeTopic(InterviewNotifyDTO interviewNotifyDTO) {
        return String.format(
                "Вы подписаны на тему:%s, из категории:%s.%s"
                        + "По вашей подписке создана новое собеседование.",
                interviewNotifyDTO.getTopicName(), interviewNotifyDTO.getCategoryName(),
                System.lineSeparator());
    }

    /**
     * Генерация сообщения для отправки при добавлении участника к собеседованию.
     *
     * @param wisherNotifyDTO WisherNotifyDTO
     * @return String message.
     */
    public static String getMessageParticipateWisher(WisherNotifyDTO wisherNotifyDTO) {
        return String.format(
                "На ваше собеседование: %s добавился участник: %s",
                wisherNotifyDTO.getInterviewTitle(),
                wisherNotifyDTO.getContactBy());
    }

    public static String getMessageApprovedWisher(WisherApprovedDTO wisherApprovedNotifyDTO) {
        return String.format(
                "Вы одобрены на собеседование: %s ",
                wisherApprovedNotifyDTO.getInterviewTitle());
    }
}
