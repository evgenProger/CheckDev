package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.UserDTO;

@Service
public class NotificationService {
    public void addSubscribeCategory(String token, UserDTO user, int categoryId) throws JsonProcessingException {
        user.getSubscribeCategoryIds().add(categoryId);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall("http://localhost:9920/user/subscribeCategoryAdd").post(
                token, mapper.writeValueAsString(user));
    }

    public void deleteSubscribeCategory(String token, UserDTO user, int categoryId) throws JsonProcessingException {
        user.getSubscribeCategoryIds().add(categoryId);
        var mapper = new ObjectMapper();
        var out = new RestAuthCall("http://localhost:9920/user/subscribeCategoryDelete").post(
                token, mapper.writeValueAsString(user));
    }

    public UserDTO findUserById(int id) throws JsonProcessingException {
        var text = new RestAuthCall("http://localhost:9920/user/" + id).get();
        var mapper = new ObjectMapper();
        UserDTO user = mapper.readValue(text, new TypeReference<>() {
        });
        return user;
    }
}