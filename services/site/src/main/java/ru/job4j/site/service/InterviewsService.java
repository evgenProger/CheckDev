package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.FilterRequestParams;
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

    public Page<InterviewDTO> getAllWithFilters(FilterRequestParams filterRequestParams, int page, int size)
            throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var headers = new HttpHeaders();
        headers.add("filter-request-params", mapper.writeValueAsString(filterRequestParams));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var text = new RestAuthCall(
                        String.format("%sgetInterviews?page=%d&size=%d", URL,
                                page, size)).getWithHeaders(headers);
        var pageType = mapper.getTypeFactory()
                .constructParametricType(RestPageImpl.class, InterviewDTO.class);
        return mapper.readValue(text, pageType);
    }
}
