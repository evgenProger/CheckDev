package ru.checkdev.mock.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.FilterProfile;
import ru.checkdev.mock.repository.FilterProfileRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class FilterProfileService {

    private final FilterProfileRepository repository;

    public List<FilterProfile> getAll() {
        return repository.findAll();
    }
}
