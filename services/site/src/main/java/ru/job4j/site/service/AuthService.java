package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.job4j.site.dto.UserInfoDTO;

import java.util.Map;

@Service
public class AuthService {
    @Value("${security.oauth2.resource.userInfoUri}")
    private String oauth2Url;

    @Value("${security.oauth2.tokenUri}")
    private String oauth2Token;

    public UserInfoDTO userInfo(String token) {
        return null;
    }

    public String token(Map<String, String> params) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(
                new RestAuthCall(oauth2Token).post(params)
        ).get("access_token").asText();
    }
}
