package ru.checkdev.forum.service;

import com.google.common.collect.Lists;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.forum.domain.Message;
import ru.checkdev.forum.domain.User;
import ru.checkdev.forum.repository.MessageRepository;
import ru.checkdev.forum.repository.SubjectRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Implementation of {@link MessageService} interface.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@Service
public class MessageServiceImpl implements MessageService {

    /**
     * Repository used to manipulate subjects in database.
     */
    private final SubjectRepository subjectRepository;

    /**
     * Repository used to manipulate messages in database.
     */
    private final MessageRepository messageRepository;

    /**
     * Service used to make calls to external microservices.
     */
    private final OAuthCall oAuthCall;

    /**
     * Constructs <code>MessageRepository</code> object.
     *
     * @param subjectRepository injected subject repository object.
     * @param messageRepository injected message repository object.
     * @param oAuthCall         injected <code>OAuthCall</code> service.
     */
    public MessageServiceImpl(final SubjectRepository subjectRepository, final MessageRepository messageRepository,
                              final OAuthCall oAuthCall) {
        this.subjectRepository = subjectRepository;
        this.messageRepository = messageRepository;
        this.oAuthCall = oAuthCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Message> getAll(final int subjectId) throws Exception {
        final List<Message> messages = Lists.newArrayList(
                this.messageRepository.findBySubjectId(subjectId,
                        new Sort(Sort.Direction.ASC, "createDate")));

        final Set<String> userKeys = new HashSet<>();
        messages.forEach(message -> userKeys.add(message.getUserKey()));

        final Map<String, User> userMap = this.oAuthCall.getUsersByKeys(userKeys);

        messages.forEach(message -> {
            if (userMap.containsKey(message.getUserKey())) {
                message.setUser(userMap.get(message.getUserKey()));
            }
        });

        return messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message getById(final int id) throws Exception {
        final Message message = this.messageRepository.findOne(id);
        if (message == null) {
            throw new DataRetrievalFailureException("Message not found");
        }

        final Map<String, User> userMap = this.oAuthCall.getUsersByKeys(
                Collections.singleton(message.getUserKey()));

        if (userMap.containsKey(message.getUserKey())) {
            message.setUser(userMap.get(message.getUserKey()));
        }

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Message add(final Message message) {
        this.subjectRepository.updateLastMessage(message.getSubject().getId(), message.getUserKey(),
                message.getCreateDate());
        return this.messageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Message save(final Message message) {
        return this.messageRepository.save(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(final int id) throws Exception {
        int subjectId = this.getById(id).getSubject().getId();
        this.messageRepository.delete(id);
        Message message = this.messageRepository.findByCreateDate(subjectId);
        this.subjectRepository.updateLastMessageAfterDeleteLastMessage(message.getSubject().getId(),
                message.getUserKey(), message.getCreateDate());
    }
}