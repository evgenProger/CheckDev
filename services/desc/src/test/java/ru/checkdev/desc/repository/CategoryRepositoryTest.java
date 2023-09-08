package ru.checkdev.desc.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.desc.domain.Category;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void whenCreatedNewCategory() {
        var category = new Category();
        category.setName("Java SE");
        var saved = categoryRepository.save(category);
        assertThat(
                categoryRepository.findById(saved.getId())
        ).isEqualTo(Optional.of(saved));
    }
}