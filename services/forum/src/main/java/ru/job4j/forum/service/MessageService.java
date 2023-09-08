package ru.checkdev.forum.service;

import ru.checkdev.forum.domain.Message;

import java.util.List;

/**
 * Service used to perform actions on messages.
 *
 * @author LightStar
 * @since 01.06.2017
 */
public interface MessageService {

    /**
     * Get all messages in given subject.
     *
     * @param subjectId id of subject that contains returned messages.
     * @return list of found messages.
     * @throws Exception thrown if can't get messages or its authors.
     */
    List<Message> getAll(int subjectId) throws Exception;

    /**
     * Get message by specific id.
     *
     * @param id message's id.
     * @return found message.
     * @throws Exception thrown if can't get message or its author.
     */
    Message getById(int id) throws Exception;

    /**
     * Add new message.
     *
     * @param message message object to add.
     * @return added message.
     */
    Message add(Message message);

    /**
     * Save message.
     *
     * @param message message object to save.
     * @return saved message.
     */
    Message save(Message message);

    /**
     * Delete message.
     *
     * @param id id of message to delete.
     */
    void delete(int id) throws Exception;
}