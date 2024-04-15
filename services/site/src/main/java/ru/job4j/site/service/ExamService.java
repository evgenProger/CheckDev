package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.site.domain.Exam;
import ru.job4j.site.util.RestAuthCall;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
@Slf4j
public class ExamService {
    private final String urlGenerator;

    public ExamService(@Value("${service.generator}") String urlGenerator) {
        this.urlGenerator = urlGenerator;
    }

    public Exam create(String token, String vacancyLink) throws JsonProcessingException, UnsupportedEncodingException {
        String encodedUrl = URLEncoder.encode(vacancyLink, "UTF-8");
        var text = new RestAuthCall(urlGenerator + "/exam/create/?url=" + encodedUrl)
                .get(token);
        return new ObjectMapper().readValue(text, Exam.class);
    }
}
