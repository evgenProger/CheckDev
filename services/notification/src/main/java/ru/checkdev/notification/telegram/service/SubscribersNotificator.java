package ru.checkdev.notification.telegram.service;

import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Profile("default")
@Service
public class SubscribersNotificator implements Notificator{

    @Value("${tg.token}")
    private String token;

    public String sendMessage(long chatId, String message) {
        String result = "";
        if (chatId > 0 && message != null) {
            RestTemplate restTemplate = new RestTemplate();
            String messageEndpoint =
                    String.format("https://api.telegram.org/bot%s/sendMessage", token);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put("chat_id", chatId);
            jsonMessage.put("text", message);
            HttpEntity<String> request = new HttpEntity<>(jsonMessage.toString(), headers);
            result = restTemplate.postForObject(messageEndpoint, request, String.class);
        }
        return result;
    }
}
