package ru.job4j.site.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * CheckDev пробное собеседование
 * Класс обработки неблокирующих запросов get и post(Multipart) в сервис AUTH
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 19.09.2023
 */
@Service
@Slf4j
@AllArgsConstructor
public class WebClientAuthCall {
    private WebClient webClient;

    /**
     * Метод doGet отправляет get запрос в сервис AUTH
     *
     * @param url   составной адрес сервиса
     * @param token "Authorization", "Bearer + token" доступ
     * @return Mono<String>
     */
    public Mono<String> doGet(String url, String token) {
        return webClient
                .get()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> log.error("API not found: {}", err.getMessage()));
    }

    /**
     * Метод doPostMultipart отправляет post запрос в сервис AUTH.
     * Тело запроса содержит MULTIPART_FROM_DATA,
     * многосоставное тела которое содержит объекты различного типа.
     *
     * @param url     составной адрес сервиса
     * @param token   "Authorization", "Bearer + token" доступ
     * @param builder MultipartBodyBuilder
     * @return Mono<ResponseEntity < String>>
     */
    public Mono<ResponseEntity<String>> doPostMultipart(String url, String token, MultipartBodyBuilder builder) {
        return webClient
                .post()
                .uri(url)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(builder.build()))
                .retrieve()
                .toEntity(String.class)
                .doOnError(err -> log.error("API not found: {}", err.getMessage()));
    }
}