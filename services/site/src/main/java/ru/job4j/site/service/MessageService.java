package ru.job4j.site.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public void delete(String token, int messageId) {
        new RestAuthCall(
                String.format("http://localhost:9920/messages/delete/%d", messageId))
                .delete(token, "");
    }

    public void setRead(String token, int messageId) {
        new RestAuthCall(
                String.format("http://localhost:9920/messages/setRead/%d", messageId))
                .put(token, "");
    }

    public void setReadAll(String token, int userId) {
        System.out.println("FROM MESSAGE SERVICE USERID" + userId);
        new RestAuthCall(
                String.format("http://localhost:9920/messages/setReadAll/%d", userId))
                .put(token, "");
    }
}
