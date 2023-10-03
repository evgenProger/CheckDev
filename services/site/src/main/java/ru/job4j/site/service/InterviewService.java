package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.InterviewDTO;
import java.time.LocalDateTime;

@Service
public class InterviewService {

    public InterviewDTO create(String token, InterviewDTO interviewDTO) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        interviewDTO.setCreateDate(LocalDateTime.now().toString());
        var out = new RestAuthCall("http://localhost:9912/interview/").post(
                token,
                mapper.writeValueAsString(interviewDTO)
        );
        return mapper.readValue(out, InterviewDTO.class);
    }
}
