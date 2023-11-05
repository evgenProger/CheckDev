package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.util.RestPageImpl;

import java.util.List;

@Service
public class InterviewsService {

    private static final String URL = "http://localhost:9912/interviews/";

    public Page<InterviewDTO> getAll(String token, int page, int size)
            throws JsonProcessingException {
        var text = new RestAuthCall(String
                .format("%s?page=%d&?size=%d", URL, page, size))
                .get(token);
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public List<InterviewDTO> getByType(int type) throws JsonProcessingException {
        var text = new RestAuthCall(String.format("%s%d", URL, type))
                .get();
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    public Page<InterviewDTO> getByTopicId(int topicId, int page, int size)
            throws JsonProcessingException {
        var text =
                new RestAuthCall(String
                        .format("%sfindByTopicId/%d?page=%d&size=%d", URL, topicId, page, size)).get();
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getByTopicsIds(List<Integer> topicIds, int page, int size)
            throws JsonProcessingException {
        var tids = parseIdsListToString(topicIds);
        var mapper = new ObjectMapper();
        var text =
                new RestAuthCall(String
                        .format("%sfindByTopicsIds/%s?page=%d&size=%d", URL, tids, page, size)).get();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getByTopicIdAndSubmitterId(
            int topicId, int submitterId, boolean not, int page, int size)
            throws JsonProcessingException {
        var text =
                new RestAuthCall(String
                        .format("%sfindByTopicId/%d?page=%d&size=%d&submitterId=%d&not=%b",
                                URL, topicId, page, size, submitterId, not)).get();
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getByTopicsIdsAndSubmitterId(
            List<Integer> topicIds, int submitterId, boolean not, int page, int size)
            throws JsonProcessingException {
        var tids = parseIdsListToString(topicIds);
        var mapper = new ObjectMapper();
        var text =
                new RestAuthCall(String
                        .format("%sfindByTopicsIds/%s?page=%d&size=%d&submitterId=%d&not=%b",
                                URL, tids, page, size, submitterId, not)).get();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getByTopicIdAndUserIdAsWisher(
            int topicId, int userId, boolean not, int page, int size)
            throws JsonProcessingException {
        var text =
                new RestAuthCall(String
                        .format("%sfindByTopicId/%d?page=%d&size=%d&userId=%d&not=%b",
                                URL, topicId, page, size, userId, not)).get();
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getByTopicsIdsAndUserIdAsWisher(
            List<Integer> topicIds, int userId, boolean not, int page, int size)
            throws JsonProcessingException {
        var tids = parseIdsListToString(topicIds);
        var mapper = new ObjectMapper();
        var text =
                new RestAuthCall(String
                        .format("%sfindByTopicsIds/%s?page=%d&size=%d&userId=%d&not=%b",
                                URL, tids, page, size, userId, not)).get();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    private String parseIdsListToString(List<Integer> list) {
        var builder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));
            if (i < list.size() - 1) {
                builder.append(',');
            }
        }
        return builder.toString();
    }

    public Page<InterviewDTO> getBySubmitterId(
            int submitterId, boolean not, int page, int size)
            throws JsonProcessingException {
        var text =
                new RestAuthCall(String
                        .format("%sfindBySubmitter/%d?page=%d&size=%d&not=%b",
                                URL, submitterId, page, size, not)).get();
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getByUserIdAsWisher(int userId, boolean not, int page, int size)
            throws JsonProcessingException {
        var text =
                new RestAuthCall(String
                        .format("%sfindByWisher/%d?page=%d&size=%d&&not=%b",
                                URL, userId, page, size, not)).get();
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }
}
