package ru.job4j.site.service;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@AllArgsConstructor
public class RestAuthCall {
    private final String url;

    public String get(String token) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + token);
        return restTemplate.exchange(url, HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<String>() { }
        ).getBody();
    }

    public String post(Map<String, String> params) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic am9iNGo6cGFzc3dvcmQ=");
        var map = new LinkedMultiValueMap<String, String>();
        params.forEach(map::add);
        map.add("scope", "any");
        map.add("grant_type", "password");
        return restTemplate.postForEntity(
                url, new HttpEntity<>(map, headers), String.class
        ).getBody();
    }
}
