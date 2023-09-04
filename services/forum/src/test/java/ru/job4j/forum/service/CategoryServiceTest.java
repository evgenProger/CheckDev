package ru.checkdev.forum.service;

import org.hamcrest.Matchers;
import org.hamcrest.core.IsSame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.forum.TestConstants;
import ru.checkdev.forum.domain.Category;
import ru.checkdev.forum.repository.CategoryRepository;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * <code>CategoryService</code> tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class CategoryServiceTest extends Mockito {

    /**
     * Mocked category repository bean.
     */
    @MockBean
    private CategoryRepository categoryRepository;

    /**
     * <code>CategoryService</code> object used in all tests.
     */
    @Autowired
    private CategoryService categoryService;

    /**
     * Test correctness of <code>getAll</code> method.
     */
    @Test
    public void whenGetAllThenResult() {
        final Category category1 = new Category();
        final Category category2 = new Category();

        final Page<Category> page = new PageImpl<>(Arrays.asList(category1, category2));
        when(this.categoryRepository.findAll(any(Sort.class))).thenReturn(page);

        final List<Category> categories = this.categoryService.getAll();

        assertThat(categories, hasSize(2));
        assertThat(categories, Matchers.contains(category1, category2));
    }

    /**
     * Test correctness of <code>getById</code> method.
     */
    @Test
    public void whenGetByIdThenResult() {
        final Category category = new Category();
        when(this.categoryRepository.findOne(TestConstants.CATEGORY_ID)).thenReturn(category);

        final Category foundCategory = this.categoryService.getById(TestConstants.CATEGORY_ID);

        assertThat(foundCategory, IsSame.sameInstance(category));
    }

    /**
     * Test correctness of <code>save</code> method.
     */
    @Test
    public void whenSaveThenItSaves() {
        final Category category = new Category();
        category.setName(TestConstants.CATEGORY_NAME);
        category.setDescription(TestConstants.CATEGORY_DESCRIPTION);
        when(this.categoryRepository.save(category)).thenReturn(category);

        final Category savedCategory = this.categoryService.save(category);

        assertThat(savedCategory, IsSame.sameInstance(category));
        verify(this.categoryRepository, times(1)).save(category);
        verifyNoMoreInteractions(this.categoryRepository);
    }

    /**
     * Test correctness of <code>delete</code> method.
     */
    @Test
    public void whenDeleteThenItDeletes() {
        this.categoryService.delete(TestConstants.CATEGORY_ID);

        verify(this.categoryRepository, times(1)).delete(TestConstants.CATEGORY_ID);
        verifyNoMoreInteractions(this.categoryRepository);
    }
}