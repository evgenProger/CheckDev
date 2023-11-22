package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.*;

import java.util.List;

@Service
@Slf4j
public class NotificationService {

    @Value("${service.notification}")
    private String urlNtf;

    public void addSubscribeCategory(String token, int userId, int categoryId) throws JsonProcessingException {
        SubscribeCategory subscribeCategory = new SubscribeCategory(userId, categoryId);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall("http://localhost:9920/subscribeCategory/add").post(
                token, mapper.writeValueAsString(subscribeCategory));
    }

    public void deleteSubscribeCategory(String token, int userId, int categoryId) throws JsonProcessingException {
        SubscribeCategory subscribeCategory = new SubscribeCategory(userId, categoryId);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall("http://localhost:9920/subscribeCategory/delete").post(
                token, mapper.writeValueAsString(subscribeCategory));
    }

    public UserDTO findCategoriesByUserId(int id) throws JsonProcessingException {
        var text = new RestAuthCall("http://localhost:9920/subscribeCategory/" + id).get();
        var mapper = new ObjectMapper();
        List<Integer> list = mapper.readValue(text, new TypeReference<>() {
        });
        return new UserDTO(id, list);
    }

    public void addSubscribeTopic(String token, int userId, int topicId) throws JsonProcessingException {
        SubscribeTopicDTO subscribeTopicDTO = new SubscribeTopicDTO(userId, topicId);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall("http://localhost:9920/subscribeTopic/add").post(
                token, mapper.writeValueAsString(subscribeTopicDTO));
    }

    public void deleteSubscribeTopic(String token, int userId, int topicId) throws JsonProcessingException {
        SubscribeTopicDTO subscribeTopic = new SubscribeTopicDTO(userId, topicId);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall("http://localhost:9920/subscribeTopic/delete").post(
                token, mapper.writeValueAsString(subscribeTopic));
    }

    public UserTopicDTO findTopicByUserId(int id) throws JsonProcessingException {
        var text = new RestAuthCall("http://localhost:9920/subscribeTopic/" + id).get();
        var mapper = new ObjectMapper();
        List<Integer> list = mapper.readValue(text, new TypeReference<>() {
        });
        return new UserTopicDTO(id, list);
    }

    public List<InnerMessageDTO> findBotMessageByUserId(String token, int id) throws JsonProcessingException {
        String url = urlNtf + "/messages/actual/" + id;
        var text = new RestAuthCall(url).get(token);
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    public void notifyAboutInterviewCreation(String token, CategoryWithTopicDTO categoryAndTopicIds)
            throws JsonProcessingException {
        var mapper = new ObjectMapper();
        new RestAuthCall(String.format("%s%s", urlNtf, "/messages/newInterview"))
                .post(token, mapper.writeValueAsString(categoryAndTopicIds));
    }

    public void sendFeedBackNotification(String token, InnerMessageDTO innerMessage) throws JsonProcessingException {
        String url = urlNtf + "/messages/message";
        var mapper = new ObjectMapper();
        var out = new RestAuthCall(url).post(
                token, mapper.writeValueAsString(innerMessage));
    }

    /**
     * Метод отправляет запрос в сервис Notification.
     * Запрос для отправки подписчикам темы о том, что появилось новое интервью.
     *
     * @param token              String
     * @param interviewNotifiDTO InterviewNotifiDTO
     * @throws JsonProcessingException Exception
     */
    public void sendSubscribeTopic(String token, InterviewNotifiDTO interviewNotifiDTO) throws JsonProcessingException {
        var url = String.format("%s/notification/topic/", urlNtf);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall(url).post(
                token, mapper.writeValueAsString(interviewNotifiDTO));
    }

    /**
     * Метод оправляет запрос в сервис Notification.
     * Запрос для отправки автору собеседования о том что добавился участник.
     *
     * @param token           String
     * @param wisherNotifiDTO WisherNotifiDTO
     */
    public void sendParticipateAuthor(String token, WisherNotifiDTO wisherNotifiDTO) {
        var url = String.format("%s/notification/participate/", urlNtf);
        var mapper = new ObjectMapper();
        try {
            var out = new RestAuthCall(url).post(
                    token, mapper.writeValueAsString(wisherNotifiDTO));
        } catch (Exception e) {
            log.error("API notification not found, error: {}", e);
        }
    }
}