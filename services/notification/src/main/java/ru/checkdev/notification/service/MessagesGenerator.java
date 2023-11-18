package ru.checkdev.notification.service;

import org.springframework.stereotype.Component;
import ru.checkdev.notification.dto.InterviewNotifDTO;

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
     * @param interviewNotifDTO InterviewNotifDTO
     * @return String
     */
    public static String generatorMessageSubscribeTopic(InterviewNotifDTO interviewNotifDTO) {
        return String.format(
                "Вы подписаны на тему:%1$s, из категории:%2$s.%3$s"
                        + "По вашей подписке создана новое собеседование.",
                interviewNotifDTO.getTopicName(), interviewNotifDTO.getCategoryName(),
                System.lineSeparator());
    }
}
