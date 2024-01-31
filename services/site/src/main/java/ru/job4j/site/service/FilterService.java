package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.job4j.site.domain.FilterProfile;
import ru.job4j.site.dto.FilterDTO;

import java.util.Collection;
import java.util.List;

@Service
public class FilterService {

    private static final String URL = "http://localhost:9912/filter/";

    public FilterDTO save(String token, FilterDTO filter) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var out = new RestAuthCall(URL).post(
                token,
                mapper.writeValueAsString(filter)
        );
        return mapper.readValue(out, FilterDTO.class);
    }

    public FilterDTO getByUserId(String token, int userId) throws JsonProcessingException {
        var text = new RestAuthCall(String.format("%s%d", URL, userId))
                .get(token);
        return new ObjectMapper().readValue(text, new TypeReference<>() {
        });
    }

    public void deleteByUserId(String token, int userId) throws JsonProcessingException {
        var mapper = new ObjectMapper();
        new RestAuthCall(String.format("%sdelete/%d", URL, userId)).delete(
                token,
                mapper.writeValueAsString(userId)
        );
    }

    public List<FilterProfile> getProfiles() throws JsonProcessingException {
        var mapper = new ObjectMapper();
        var text = new RestAuthCall(URL + "/profiles").get();
        return mapper.readValue(text, new TypeReference<>() {
        });
    }

    public String getNameById(Collection<FilterProfile> filterProfiles, int id) {
        String result = "";
        for (var filterProfile : filterProfiles) {
            if (filterProfile.getId() == id) {
                result = filterProfile.getName();
                break;
            }
        }
        return result;
    }
}
