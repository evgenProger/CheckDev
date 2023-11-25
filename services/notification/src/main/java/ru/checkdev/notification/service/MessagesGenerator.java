package ru.checkdev.notification.service;

import org.springframework.stereotype.Component;
import ru.checkdev.notification.dto.InterviewNotifiDTO;
import ru.checkdev.notification.dto.WisherApprovedDTO;
import ru.checkdev.notification.dto.WisherNotifiDTO;

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
     * @param interviewNotifiDTO InterviewNotifDTO
     * @return String
     */
    public static String getMessageSubscribeTopic(InterviewNotifiDTO interviewNotifiDTO) {
        return String.format(
                "Вы подписаны на тему:%s, из категории:%s.%s"
                        + "По вашей подписке создана новое собеседование.",
                interviewNotifiDTO.getTopicName(), interviewNotifiDTO.getCategoryName(),
                System.lineSeparator());
    }

    /**
     * Генерация сообщения для отправки при добавлении участника к собеседованию.
     *
     * @param wisherNotifiDTO WisherNotifiDTO
     * @return String message.
     */
    public static String getMessageParticipateWisher(WisherNotifiDTO wisherNotifiDTO) {
        return String.format(
                "На ваше собеседование: %s добавился участник: %s",
                wisherNotifiDTO.getInterviewTitle(),
                wisherNotifiDTO.getContactBy());
    }

    public static String getMessageApprovedWisher(WisherApprovedDTO wisherApprovedNotifyDTO) {
        return String.format(
                "Вы одобрены на собеседование: %s ",
                wisherApprovedNotifyDTO.getInterviewTitle());
    }
}
