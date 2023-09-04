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
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.forum.TestConstants;
import ru.checkdev.forum.domain.Subject;
import ru.checkdev.forum.repository.SubjectRepository;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;

/**
 * <code>SubjectService</code> tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SubjectServiceTest extends Mockito {

    /**
     * Mocked subject repository bean.
     */
    @MockBean
    private SubjectRepository subjectRepository;

    /**
     * Mocked <code>OAuthCall</code> service.
     */
    @MockBean
    private OAuthCall oAuthCall;

    /**
     * <code>SubjectService</code> object used in all tests.
     */
    @Autowired
    private SubjectService subjectService;

    /**
     * Test correctness of <code>getAll</code> method.
     */
    @Test
    public void whenGetAllThenResult() throws Exception {
        final Subject subject1 = new Subject();
        final Subject subject2 = new Subject();

        final Page<Subject> page = new PageImpl<>(Arrays.asList(subject1, subject2));
        when(this.oAuthCall.getUsersByKeys(any())).thenReturn(Collections.emptyMap());
        when(this.subjectRepository.findByCategoryId(eq(TestConstants.CATEGORY_ID), any(Pageable.class)))
                .thenReturn(page);

        final Page<Subject> subjects = this.subjectService.getAll(TestConstants.CATEGORY_ID, 0);

        assertThat(subjects, iterableWithSize(2));
        assertThat(subjects, Matchers.contains(subject1, subject2));
    }

    /**
     * Test correctness of <code>getById</code> method.
     */
    @Test
    public void whenGetByIdThenResult() throws Exception {
        final Subject subject = new Subject();
        when(this.oAuthCall.getUsersByKeys(any())).thenReturn(Collections.emptyMap());
        when(this.subjectRepository.findOne(TestConstants.SUBJECT_ID)).thenReturn(subject);

        final Subject foundSubject = this.subjectService.getById(TestConstants.SUBJECT_ID);

        assertThat(foundSubject, IsSame.sameInstance(subject));
    }

    /**
     * Test correctness of <code>save</code> method.
     */
    @Test
    public void whenSaveThenItSaves() {
        final Subject subject = new Subject();
        subject.setName(TestConstants.SUBJECT_NAME);
        subject.setDescription(TestConstants.SUBJECT_DESCRIPTION);
        when(this.subjectRepository.save(subject)).thenReturn(subject);

        final Subject savedSubject = this.subjectService.save(subject);

        assertThat(savedSubject, IsSame.sameInstance(subject));
        verify(this.subjectRepository, times(1)).save(subject);
        verifyNoMoreInteractions(this.subjectRepository);
    }

    /**
     * Test correctness of <code>delete</code> method.
     */
    @Test
    public void whenDeleteThenItDeletes() {
        this.subjectService.delete(TestConstants.SUBJECT_ID);

        verify(this.subjectRepository, times(1)).delete(TestConstants.SUBJECT_ID);
        verifyNoMoreInteractions(this.subjectRepository);
    }
}