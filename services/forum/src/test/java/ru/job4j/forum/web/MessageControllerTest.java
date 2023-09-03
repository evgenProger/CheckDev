package ru.job4j.forum.web;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import ru.job4j.forum.TestConstants;
import ru.job4j.forum.domain.Message;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <code>MessageController</code> tests.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public class MessageControllerTest extends ControllerTest {

    /**
     * Test correctness of get all messages request.
     */
    @Test
    public void whenListThenResult() throws Exception {
        when(this.messageService.getAll(TestConstants.SUBJECT_ID))
                .thenReturn(Arrays.asList(this.message, this.message2));

        this.mvc.perform(this.getJson(String.format("/forum/subject/%d/message/", TestConstants.SUBJECT_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(String.format("[%s,%s]",
                        TestConstants.MESSAGE_JSON, TestConstants.MESSAGE_JSON2)));

        verify(this.messageService, times(1)).getAll(TestConstants.MESSAGE_ID);
        verifyNoMoreInteractions(this.messageService);
    }

    /**
     * Test correctness of get one message request.
     */
    @Test
    public void whenGetThenResult() throws Exception {
        when(this.messageService.getById(TestConstants.MESSAGE_ID)).thenReturn(this.message);

        this.mvc.perform(this.getJson(String.format("/forum/message/%d", TestConstants.MESSAGE_ID)))
                .andExpect(status().isOk())
                .andExpect(content().json(TestConstants.MESSAGE_JSON));

        verify(this.messageService, times(1)).getById(TestConstants.MESSAGE_ID);
        verifyNoMoreInteractions(this.messageService);
    }

    /**
     * Test correctness of add message request.
     */
    @Test
    public void whenAddThenItAdds() throws Exception {
        final ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        when(this.messageService.add(captor.capture())).thenReturn(this.message);

        this.mvc.perform(this.makeAuthorized(this.postJson(String.format(
                        "/forum/subject/%d/message/", TestConstants.SUBJECT_ID)))
                    .content(TestConstants.MESSAGE_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(TestConstants.MESSAGE_JSON));

        verify(this.messageService, times(1)).add(any(Message.class));
        verifyNoMoreInteractions(this.messageService);

        assertThat(captor.getValue().getText(), is(TestConstants.MESSAGE_TEXT));
        assertThat(captor.getValue().getUserKey(), is(TestConstants.OAUTH_USER_KEY));
    }

    /**
     * Test correctness of update message request.
     */
    @Test
    public void whenUpdateThenItUpdates() throws Exception {
        when(this.messageService.getById(TestConstants.MESSAGE_ID)).thenReturn(this.message);

        final ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        when(this.messageService.save(captor.capture())).thenReturn(this.message);

        this.mvc.perform(this.makeAuthorized(this.putJson(String.format(
                        "/forum/message/%d", TestConstants.MESSAGE_ID)))
                    .content(TestConstants.MESSAGE_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(TestConstants.MESSAGE_JSON));

        verify(this.messageService, times(1)).getById(TestConstants.MESSAGE_ID);
        verify(this.messageService, times(1)).save(any(Message.class));
        verifyNoMoreInteractions(this.messageService);

        assertThat(captor.getValue().getText(), is(TestConstants.MESSAGE_TEXT));
    }

    /**
     * Test correctness of delete message request.
     */
    @Test
    public void whenDeleteThenItDeletes() throws Exception {
        when(this.messageService.getById(TestConstants.MESSAGE_ID)).thenReturn(this.message);

        this.mvc.perform(this.makeAuthorized(delete(String.format("/forum/message/%d", TestConstants.MESSAGE_ID))))
                .andExpect(status().isNoContent());

        verify(this.messageService, times(1)).getById(TestConstants.MESSAGE_ID);
        verify(this.messageService, times(1)).delete(TestConstants.MESSAGE_ID);
        verifyNoMoreInteractions(this.messageService);
    }
}