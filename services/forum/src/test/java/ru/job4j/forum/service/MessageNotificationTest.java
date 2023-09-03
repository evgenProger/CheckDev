package ru.job4j.forum.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.forum.domain.Category;
import ru.job4j.forum.domain.Message;
import ru.job4j.forum.domain.Subject;

import java.util.Collections;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * <code>MessageNotificationTest</code> class.
 *
 * @author LightStar
 * @since 22.06.2017
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class MessageNotificationTest extends Mockito {

    @Autowired
    private MessageNotification messageNotification;

    @Autowired
    private SubjectService subjects;

    @Autowired
    private MessageService messages;

    @Autowired
    private CategoryService categories;

    @MockBean
    private OAuthCall oAuthCall;

    @Test
    public void whenSendInSchedulerThenOk() throws Exception {
        Category category = this.categories.save(new Category());
        Subject subject = new Subject();
        subject.setCategory(category);
        subject = subjects.save(subject);
        Message message = new Message();
        message.setSubject(subject);
        message = messages.save(message);
                final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        when(this.oAuthCall.getUsersByKeys(any())).thenReturn(Collections.emptyMap());
      final int messageId = message.getId();
        scheduler.submit(() -> {
            try {
                this.messageNotification.send(messageId, -1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).get();
        scheduler.shutdown();
        while (!scheduler.isTerminated()) {
        }
    }
}