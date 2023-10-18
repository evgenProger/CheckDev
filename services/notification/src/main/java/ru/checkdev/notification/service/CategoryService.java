package ru.checkdev.notification.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.checkdev.notification.domain.Category;
import ru.checkdev.notification.repository.CategoryRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {

    private final CategoryRepository repository;
    public void save(Category category) {
        repository.save(category);
    }

    public Optional<Category> findById(int id) {
        return repository.findById(id);
    }
}