package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.*;
import ru.job4j.site.util.RestAuthCall;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationService {

    private final String urlNtf;

    public NotificationService(@Value("${service.notification}") String urlNtf) {
        this.urlNtf = urlNtf;
    }

    public void addSubscribeCategory(String token, int userId, int categoryId) {
        SubscribeCategory subscribeCategory = new SubscribeCategory(userId, categoryId);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall("http://localhost:9920/subscribeCategory/add").post(
                    token, mapper.writeValueAsString(subscribeCategory));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    public void deleteSubscribeCategory(String token, int userId, int categoryId) {
        SubscribeCategory subscribeCategory = new SubscribeCategory(userId, categoryId);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall("http://localhost:9920/subscribeCategory/delete").post(
                    token, mapper.writeValueAsString(subscribeCategory));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    public Optional<UserDTO> findCategoriesByUserId(int id) {
        var mapper = new ObjectMapper();
        try {
            var text = new RestAuthCall("http://localhost:9920/subscribeCategory/" + id).get();
            List<Integer> list = mapper.readValue(text, new TypeReference<>() {
            });
            return Optional.of(new UserDTO(id, list));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public void addSubscribeTopic(String token, int userId, int topicId) {
        SubscribeTopicDTO subscribeTopicDTO = new SubscribeTopicDTO(userId, topicId);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall("http://localhost:9920/subscribeTopic/add").post(
                    token, mapper.writeValueAsString(subscribeTopicDTO));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    public void deleteSubscribeTopic(String token, int userId, int topicId) {
        SubscribeTopicDTO subscribeTopic = new SubscribeTopicDTO(userId, topicId);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall("http://localhost:9920/subscribeTopic/delete").post(
                    token, mapper.writeValueAsString(subscribeTopic));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    public Optional<UserTopicDTO> findTopicByUserId(int id) {
        var mapper = new ObjectMapper();
        try {
            var text = new RestAuthCall("http://localhost:9920/subscribeTopic/" + id).get();
            List<Integer> list = mapper.readValue(text, new TypeReference<>() {
            });
            return Optional.of(new UserTopicDTO(id, list));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public List<InnerMessageDTO> findBotMessageByUserId(String token, int id) {
        String url = urlNtf + "/messages/actual/" + id;
        var mapper = new ObjectMapper();
        try {
            var text = new RestAuthCall(url).get(token);
            return mapper.readValue(text, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
            return Collections.emptyList();
        }
    }

    public void notifyAboutInterviewCreation(String token, CategoryWithTopicDTO categoryAndTopicIds) {
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall(String.format("%s%s", urlNtf, "/messages/newInterview"))
                    .post(token, mapper.writeValueAsString(categoryAndTopicIds));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    public void sendFeedBackMessage(String token, InnerMessageDTO innerMessage) {
        String url = urlNtf + "/messages/message";
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(innerMessage));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    public void sendFeedbackNotification(String token, FeedbackNotificationDTO feedbackNotification) {
        String url = urlNtf + "/feedback/interview";
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(feedbackNotification));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    /**
     * Метод отправляет запрос в сервис Notification.
     * Запрос для отправки подписчикам темы о том, что появилось новое интервью.
     *
     * @param token              String
     * @param interviewNotifiDTO InterviewNotifiDTO
     * @throws JsonProcessingException Exception
     */
    public void sendSubscribeTopic(String token, InterviewNotifiDTO interviewNotifiDTO) {
        var url = String.format("%s/notification/topic/", urlNtf);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(interviewNotifiDTO));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    /**
     * Метод оправляет запрос в сервис Notification.
     * Запрос для отправки автору собеседования о том что добавился участник.
     *
     * @param token           String
     * @param wisherNotifyDTO WisherNotifyDTO
     */
    public void sendParticipateAuthor(String token, WisherNotifyDTO wisherNotifyDTO) {
        var url = String.format("%s/notification/participate/", urlNtf);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(wisherNotifyDTO));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }

    /**
     * Метод оправляет запрос в сервис Notification.
     * Запрос для отправки сообщения участнику собеседования о том что автор собеседования удалил собеседование.
     *
     * @param token              String
     * @param cancelInterviewDTO CancelInterviewNotificationDTO
     */
    public void sendParticipateCancelInterview(String token, CancelInterviewNotificationDTO cancelInterviewDTO) {
        var url = String.format("%s/notification/cancelInterview/", urlNtf);
        var mapper = new ObjectMapper();
        try {
            new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(cancelInterviewDTO));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e);
        }
    }

    public void approvedWisher(String token, WisherApprovedDTO wisherApprovedDTO) {
        var url = String.format("%s/notificationWisher/approvedWisher/", urlNtf);
        var mapper = new ObjectMapper();
        try {
            var out = new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(wisherApprovedDTO));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e.getMessage());
        }
    }
}