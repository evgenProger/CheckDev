package ru.job4j.forum.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.forum.domain.Message;
import ru.job4j.forum.domain.Notify;
import ru.job4j.forum.domain.Subject;
import ru.job4j.forum.domain.User;
import ru.job4j.forum.repository.MessageRepository;
import ru.job4j.forum.repository.SubjectRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Service used to send notification about new message.
 *
 * @author LightStar
 * @since 22.06.2017
 */
@Service
public class MessageNotification {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Object for JSON manipulating.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * URL of the notification microservice.
     */
    @Value("${server.notification}")
    private String notificationUrl;

    /**
     * Access key for notification microservice.
     */
    @Value("${access.notification}")
    private String notificationKey;

    /**
     * Service used to make calls to other microservices.
     */
    private final OAuthCall oAuthCall;

    /**
     * Message repository.
     */
    private final MessageRepository messageRepository;

    private final SubjectRepository subjects;

    @Autowired
    public MessageNotification(final OAuthCall oAuthCall, final MessageRepository messageRepository, SubjectRepository subjects) {
        this.oAuthCall = oAuthCall;
        this.messageRepository = messageRepository;
        this.subjects = subjects;
    }

    /**
     * Send notification after adding new message.
     *
     * @param messageId id of added message.
     */
    public void sendAsync(final int messageId, final int subjectId) {
        this.scheduler.execute(() -> {
            try {
                this.send(messageId, subjectId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Transactional
    public void send(final int messageId, final int subjectId) throws Exception {
        final Message message = this.messageRepository.findOne(messageId);
        final Subject subject = this.subjects.findOne(subjectId);
        final Map<String, User> userMap = this.getUsers(subject);
        final Map<String, String> params = this.getParams(
                message,
                userMap.get(message.getUserKey()), subject
        );

        for (final User user : userMap.values()) {
            if (user.getKey().equals(message.getUserKey())) {
                continue;
            }

            this.oAuthCall.doPost(
                    null,
                    String.format("%s/template/queue?access=%s", this.notificationUrl, this.notificationKey),
                    this.mapper.writeValueAsString(
                            new Notify(user.getEmail(), params, Notify.Type.FORUM_MESSAGE.name()))
            );
        }
    }

    /**
     * Get map of users to notify about added message including message's author.
     *
     * @param subject subject.
     * @return map of users to notify including message's author.
     * @throws Exception can be thrown on network error.
     */
    private Map<String, User> getUsers(final Subject subject) throws Exception {
        final Iterable<Message> messages = this.messageRepository.findBySubjectId(subject.getId(),
                new Sort(Sort.Direction.ASC, "createDate"));
        final Set<String> userKeys = new HashSet<>();
        userKeys.add(subject.getUserKey());
        messages.forEach(m -> userKeys.add(m.getUserKey()));
        return this.oAuthCall.getUsersByKeys(userKeys);
    }

    /**
     * Collect params for new message notification.
     *
     * @param message added message.
     * @param user    message's author.
     * @return collected params.
     */
    private Map<String, String> getParams(final Message message, final User user, final Subject subject) {
        final Map<String, String> params = new HashMap<>();
        params.put("subject_id", String.valueOf(subject.getId()));
        params.put("subject_title", subject.getName());
        params.put("message_text", message.getText());
        params.put("message_user", user != null ? user.getName() : "<DELETED>");
        return params;
    }
}