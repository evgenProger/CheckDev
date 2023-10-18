package ru.checkdev.notification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.domain.Category;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    public void saveAndFindById() {
        Category category = new Category(1, "Category1", null);
        categoryService.save(category);
        Category findCategory = this.categoryService.findById(1).get();
        assertEquals(category.getId(), findCategory.getId());
        assertEquals(category.getName(), findCategory.getName());
    }
}