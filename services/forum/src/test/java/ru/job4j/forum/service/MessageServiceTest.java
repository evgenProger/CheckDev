package ru.job4j.forum.service;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.forum.TestConstants;
import ru.job4j.forum.domain.Message;
import ru.job4j.forum.repository.MessageRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * <code>MessageService</code> tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MessageServiceTest extends Mockito {

    /**
     * Mocked message repository bean.
     */
    @MockBean
    private MessageRepository messageRepository;

    /**
     * Mocked <code>OAuthCall</code> service.
     */
    @MockBean
    private OAuthCall oAuthCall;

    /**
     * <code>MessageService</code> object used in all tests.
     */
    @Autowired
    private MessageService messageService;

    /**
     * Test correctness of <code>getAll</code> method.
     */
    @Test
    public void whenGetAllThenResult() throws Exception {
        final Message message1 = new Message();
        final Message message2 = new Message();

        final Page<Message> page = new PageImpl<>(Arrays.asList(message1, message2));
        when(this.oAuthCall.getUsersByKeys(any())).thenReturn(Collections.emptyMap());
        when(this.messageRepository.findBySubjectId(eq(TestConstants.SUBJECT_ID), any()))
                .thenReturn(page);

        final List<Message> messages = this.messageService.getAll(TestConstants.SUBJECT_ID);

        assertThat(messages, hasSize(2));
        assertThat(messages, Matchers.contains(message1, message2));
    }

    /**
     * Test correctness of <code>getById</code> method.
     */
    @Test
    public void whenGetByIdThenResult() throws Exception {
        final Message message = new Message();
        when(this.oAuthCall.getUsersByKeys(any())).thenReturn(Collections.emptyMap());
        when(this.messageRepository.findOne(TestConstants.MESSAGE_ID)).thenReturn(message);

        final Message foundSubject = this.messageService.getById(TestConstants.MESSAGE_ID);

        assertThat(foundSubject, IsSame.sameInstance(message));
    }

    /**
     * Test correctness of <code>save</code> method.
     */
    @Test
    public void whenSaveThenItSaves() {
        final Message message = new Message();
        message.setText(TestConstants.MESSAGE_TEXT);
        when(this.messageRepository.save(message)).thenReturn(message);

        final Message savedMessage = this.messageService.save(message);

        assertThat(savedMessage, IsSame.sameInstance(message));
        verify(this.messageRepository, times(1)).save(message);
        verifyNoMoreInteractions(this.messageRepository);
    }

    /**
     * Test correctness of <code>delete</code> method.
     */
    @Test
    public void whenDeleteThenItDeletes() {
        this.messageService.delete(TestConstants.MESSAGE_ID);

        verify(this.messageRepository, times(1)).delete(TestConstants.MESSAGE_ID);
        verifyNoMoreInteractions(this.messageRepository);
    }
}