package ru.checkdev.forum.web;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.PageImpl;
import ru.checkdev.forum.TestConstants;
import ru.checkdev.forum.domain.Subject;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <code>SubjectController</code> tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public class SubjectControllerTest extends ControllerTest {

    /**
     * Test correctness of get all subjects request.
     */
    @Test
    public void whenListThenResult() throws Exception {
        when(this.subjectService.getAll(eq(TestConstants.CATEGORY_ID), any(Integer.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(this.subject, this.subject2)));

        this.mvc.perform(this.getJson(String.format("/forum/category/%d/subject/", TestConstants.CATEGORY_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("{\"content\":[%s,%s]}",
                        TestConstants.SUBJECT_JSON, TestConstants.SUBJECT_JSON2)));

        verify(this.subjectService, times(1))
                .getAll(eq(TestConstants.CATEGORY_ID), any(Integer.class));
        verifyNoMoreInteractions(this.subjectService);
    }

    /**
     * Test correctness of get one subject request.
     */
    @Test
    public void whenGetThenResult() throws Exception {
        when(this.subjectService.getById(TestConstants.SUBJECT_ID)).thenReturn(this.subject);

        this.mvc.perform(this.getJson(String.format("/forum/subject/%d", TestConstants.SUBJECT_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(TestConstants.SUBJECT_JSON));

        verify(this.subjectService, times(1)).getById(TestConstants.SUBJECT_ID);
        verifyNoMoreInteractions(this.subjectService);
    }

    /**
     * Test correctness of add subject request.
     */
    @Test
    public void whenAddThenItAdds() throws Exception {
        final ArgumentCaptor<Subject> captor = ArgumentCaptor.forClass(Subject.class);
        when(this.subjectService.save(captor.capture())).thenReturn(this.subject);

        this.mvc.perform(this.makeAuthorized(this.postJson(String.format(
                        "/forum/category/%d/subject/", TestConstants.CATEGORY_ID)))
                    .content(TestConstants.SUBJECT_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(TestConstants.SUBJECT_JSON));

        verify(this.subjectService, times(1)).save(any(Subject.class));
        verifyNoMoreInteractions(this.subjectService);

        assertThat(captor.getValue().getName(), is(TestConstants.SUBJECT_NAME));
        assertThat(captor.getValue().getDescription(), is(TestConstants.SUBJECT_DESCRIPTION));
        assertThat(captor.getValue().getBrief(), is(TestConstants.SUBJECT_BRIEF));
        assertThat(captor.getValue().getUserKey(), is(TestConstants.OAUTH_USER_KEY));
    }

    /**
     * Test correctness of update subject request.
     */
    @Test
    public void whenUpdateThenItUpdates() throws Exception {
        when(this.subjectService.getById(TestConstants.SUBJECT_ID)).thenReturn(this.subject);

        final ArgumentCaptor<Subject> captor = ArgumentCaptor.forClass(Subject.class);
        when(this.subjectService.save(captor.capture())).thenReturn(this.subject);

        this.mvc.perform(this.makeAuthorized(this.putJson(String.format(
                        "/forum/subject/%d", TestConstants.SUBJECT_ID)))
                    .content(TestConstants.SUBJECT_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(TestConstants.SUBJECT_JSON));

        verify(this.subjectService, times(1)).getById(TestConstants.SUBJECT_ID);
        verify(this.subjectService, times(1)).save(any(Subject.class));
        verifyNoMoreInteractions(this.subjectService);

        assertThat(captor.getValue().getName(), is(TestConstants.SUBJECT_NAME));
        assertThat(captor.getValue().getDescription(), is(TestConstants.SUBJECT_DESCRIPTION));
        assertThat(captor.getValue().getBrief(), is(TestConstants.SUBJECT_BRIEF));
    }

    /**
     * Test correctness of delete subject request.
     */
    @Test
    public void whenDeleteThenItDeletes() throws Exception {
        when(this.subjectService.getById(TestConstants.SUBJECT_ID)).thenReturn(this.subject);

        this.mvc.perform(this.makeAuthorized(delete(String.format("/forum/subject/%d", TestConstants.SUBJECT_ID))))
                .andExpect(status().isNoContent());

        verify(this.subjectService, times(1)).getById(TestConstants.SUBJECT_ID);
        verify(this.subjectService, times(1)).delete(TestConstants.SUBJECT_ID);
        verifyNoMoreInteractions(this.subjectService);
    }
}