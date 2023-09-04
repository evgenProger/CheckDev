package ru.checkdev.forum.web;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.checkdev.forum.TestConstants;
import ru.checkdev.forum.domain.Category;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <code>CategoryController</code> tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public class CategoryControllerTest extends ControllerTest {

    /**
     * Test correctness of get all categories request.
     */
    @Test
    public void whenListThenResult() throws Exception {
        when(this.categoryService.getAll())
                .thenReturn(Arrays.asList(this.category, this.category2));

        this.mvc.perform(this.getJson("/forum/category/"))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("[%s,%s]", TestConstants.CATEGORY_JSON,
                        TestConstants.CATEGORY_JSON2)));

        verify(this.categoryService, times(1)).getAll();
        verifyNoMoreInteractions(this.categoryService);
    }

    /**
     * Test correctness of get one category request.
     */
    @Test
    public void whenGetThenResult() throws Exception {
        when(this.categoryService.getById(TestConstants.CATEGORY_ID)).thenReturn(this.category);

        this.mvc.perform(this.getJson(String.format("/forum/category/%d", TestConstants.CATEGORY_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(TestConstants.CATEGORY_JSON));

        verify(this.categoryService, times(1)).getById(TestConstants.CATEGORY_ID);
        verifyNoMoreInteractions(this.categoryService);
    }

    /**
     * Test correctness of add category request.
     */
    @Test
    public void whenAddThenItAdds() throws Exception {
        final ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        when(this.categoryService.save(captor.capture())).thenReturn(this.category);

        this.mvc.perform(this.makeAuthorized(this.postJson("/forum/category/"))
                    .content(TestConstants.CATEGORY_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(TestConstants.CATEGORY_JSON));

        verify(this.categoryService, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(this.categoryService);

        assertThat(captor.getValue().getName(), is(TestConstants.CATEGORY_NAME));
        assertThat(captor.getValue().getDescription(), is(TestConstants.CATEGORY_DESCRIPTION));
    }

    /**
     * Test correctness of update category request.
     */
    @Test
    public void whenUpdateThenItUpdates() throws Exception {
        when(this.categoryService.getById(TestConstants.CATEGORY_ID)).thenReturn(this.category);

        final ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        when(this.categoryService.save(captor.capture())).thenReturn(this.category);

        this.mvc.perform(this.makeAuthorized(this.putJson(String.format(
                        "/forum/category/%d", TestConstants.CATEGORY_ID)))
                    .content(TestConstants.CATEGORY_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(TestConstants.CATEGORY_JSON));

        verify(this.categoryService, times(1)).getById(TestConstants.CATEGORY_ID);
        verify(this.categoryService, times(1)).save(any(Category.class));
        verifyNoMoreInteractions(this.categoryService);

        assertThat(captor.getValue().getName(), is(TestConstants.CATEGORY_NAME));
        assertThat(captor.getValue().getDescription(), is(TestConstants.CATEGORY_DESCRIPTION));
    }

    /**
     * Test correctness of delete category request.
     */
    @Test
    public void whenDeleteThenItDeletes() throws Exception {
        this.mvc.perform(this.makeAuthorized(delete(String.format("/forum/category/%d", TestConstants.CATEGORY_ID))))
                .andExpect(status().isNoContent());

        verify(this.categoryService, times(1)).delete(TestConstants.CATEGORY_ID);
        verifyNoMoreInteractions(this.categoryService);
    }
}