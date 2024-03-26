package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.site.domain.Exam;
import ru.job4j.site.util.RestAuthCall;

@Service
@Slf4j
public class ExamService {
    private final String urlGenerator;

    public ExamService(@Value("${service.generator}") String urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    public Exam create(String token, String vacancyLink) throws JsonProcessingException {
        var text = new RestAuthCall(String.format("%s%s%s", urlGenerator, "/create/", vacancyLink))
                .get(token);
        return new ObjectMapper().readValue(text, new TypeReference<>() {
        });
    }
}
