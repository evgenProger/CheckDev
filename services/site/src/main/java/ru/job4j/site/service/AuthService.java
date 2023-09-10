package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.UserInfoDTO;

import java.util.Map;

@Service
public class AuthService {
    @Value("${security.oauth2.resource.userInfoUri}")
    private String oauth2Url;

    @Value("${security.oauth2.tokenUri}")
    private String oauth2Token;

    public UserInfoDTO userInfo(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new RestAuthCall(
                "http://localhost:9900/person/current"
        ).get(token), UserInfoDTO.class);
    }

    public String token(Map<String, String> params) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(
                new RestAuthCall(oauth2Token).token(params)
        ).get("access_token").asText();
    }
}
