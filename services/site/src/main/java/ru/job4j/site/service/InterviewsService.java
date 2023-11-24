package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.InterviewDTO;
import ru.job4j.site.util.RestPageImpl;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class InterviewsService {

    private final WisherService wisherService;

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

    public Page<InterviewDTO> getAllByUserIdRelated(String token, int page, int size, int userId)
            throws JsonProcessingException {
        var text = new RestAuthCall(String
                .format("%s/findByUserIdRelated/%s?page=%d&?size=%d", URL, userId, page, size))
                .get(token);
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }

    public Page<InterviewDTO> getAllByUserIdRelatedFiltered(String token, int page, int size, int userId, List<Integer> topicIds)
            throws JsonProcessingException {
        var tids = parseIdsListToString(topicIds);
        var text = new RestAuthCall(String
                        .format("%s/findByUserIdRelatedFiltered/%s?page=%d&size=%d&tids=%s", URL, userId, page, size, tids))
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

    public List<InterviewDTO> getLast() throws JsonProcessingException {
        String text = new RestAuthCall(String.format("%s%s", URL, "/last"))
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

    /**
     * Метод выполняет set поля countWishers(количество откликов) модели Интервью
     *
     * @param interviewsDTO interviewsDTO
     * @param token         token
     * @throws JsonProcessingException
     */
    public void setCountWishers(List<InterviewDTO> interviewsDTO, String token)
            throws JsonProcessingException {
        for (var interviewDTO : interviewsDTO) {
            var wishers = wisherService.getAllWisherDtoByInterviewId(
                    token, String.valueOf(interviewDTO.getId()));
            Long countWishers = wisherService.countWishers(wishers, interviewDTO.getId());
            interviewDTO.setCountWishers(countWishers);
        }
    }

    /**
     * Метод получает из REST сервиса MOCK все собеседования,
     * на которые пользователь должен оставить отзыв
     *
     * @param userId ID User
     * @return List<Interview>
     */
    public List<InterviewDTO> findAllIdByNoFeedback(int userId) {
        List<InterviewDTO> result = new ArrayList<>();
        String url = String.format("http://localhost:9912/interviews/noFeedback/%d", userId);
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonText = new RestAuthCall(url).get();
            result = mapper.readValue(jsonText, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("MOCK API is not available, error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * Метод получает из REST сервиса MOCK все новые собеседования,
     *
     * @return List<Interview>
     */
    public List<InterviewDTO> getNewInterviews() {
        List<InterviewDTO> result = new ArrayList<>();
        String url = String.format("http://localhost:9912/interviews/interviewStatusNew");
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonText = new RestAuthCall(url).get();
            result = mapper.readValue(jsonText, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("MOCK API is not available, error: {}", e.getMessage());
        }
        return result;
    }

    /**
     * Метод выполняет подсчет количества интервью по topicId
     * @param topicId topicId
     * @return количество интервью по topicId
     */
    public Long  countNewInterviewsByTopic(int topicId) {
        List<InterviewDTO> list = getNewInterviews();
       return list.stream().map(interviewDTO -> interviewDTO.getTopicId())
               .filter(integer -> integer.equals(topicId)).count();
    }
}
