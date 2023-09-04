package ru.checkdev.template;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

public class Client {

    public static class Category {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer GGX4rTSqI8Wc2y5FOWo10Ci6txM");
        var out = restTemplate.exchange("http://localhost:9902/categories/", HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<String>() { }
        ).getBody();
        System.out.println(out);
    }
}
