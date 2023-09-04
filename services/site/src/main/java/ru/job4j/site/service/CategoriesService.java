package ru.job4j.site.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import ru.job4j.site.dto.CategoryDTO;

import java.util.List;

@Service
public class CategoriesService {
    public List<CategoryDTO> getAll(String token) throws JsonProcessingException {
        var text = new RestAuthCall("http://localhost:9902/categories/").get(token);
        var mapper = new ObjectMapper();
        return mapper.readValue(text, new TypeReference<>(){});
    }
}
