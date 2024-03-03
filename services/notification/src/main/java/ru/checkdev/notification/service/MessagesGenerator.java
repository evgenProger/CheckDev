package ru.checkdev.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.checkdev.notification.dto.CancelInterviewNotificationDTO;
import ru.checkdev.notification.dto.InterviewNotifyDTO;
import ru.checkdev.notification.dto.WisherApprovedDTO;
import ru.checkdev.notification.dto.WisherNotifyDTO;

import java.util.StringJoiner;

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

    @Value("${service.urlSite}")
    private String urlSite;

    private static String urlSiteStatic;

    @Value("${service.urlSite}")
    public void setUrlSiteStatic(String urlSite) {
        MessagesGenerator.urlSiteStatic = urlSite;
    }

    public String getMessageSubscribeTopic(InterviewNotifyDTO interviewNotifyDTO) {
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
    public String getMessageParticipateWisher(WisherNotifyDTO wisherNotifyDTO) {
        return "На ваше собеседование: "
                + wisherNotifyDTO.getInterviewTitle()
                + " добавился участник: "
                + wisherNotifyDTO.getUserName()
                + System.lineSeparator()
                + "Ссылка на собеседование: "
                + urlSiteStatic
                + "/interview/"
                + wisherNotifyDTO.getInterviewId();
    }

    /**
     * Генерация сообщения для отправки участнику при отмене собеседования его автором.
     *
     * @param cancelInterviewDTO CancelInterviewNotificationDTO
     * @return String message.
     */
    public String getMessageCancelInterview(CancelInterviewNotificationDTO cancelInterviewDTO) {
        StringJoiner joiner = new StringJoiner("");
        return joiner.add("Собеседование ")
                .add(cancelInterviewDTO.getInterviewTitle())
                .add(", на которое вы откликнулись, было отменено создателем ")
                .add(cancelInterviewDTO.getSubmitterName())
                .add(". Причина отмены собеседования: \"")
                .add(cancelInterviewDTO.getReasonOfCancel())
                .add("\". Данное собеседование вам больше недоступно.").toString();
    }

    public String getMessageApprovedWisher(WisherApprovedDTO wisherApprovedNotifyDTO) {
        return String.format("Вы приглашены на собеседование: %s.%sСсылка на собеседование: %s",
                wisherApprovedNotifyDTO.getInterviewTitle(),
                System.lineSeparator(),
                wisherApprovedNotifyDTO.getInterviewLink()
        );
    }
}
