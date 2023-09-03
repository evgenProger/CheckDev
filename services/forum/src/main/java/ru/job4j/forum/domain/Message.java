package ru.job4j.forum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Forum message model.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@Entity(name = "message")
public class Message  implements Serializable {

    /**
     * Message's id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private int id = -1;

    /**
     * Subject in which message is contained.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="subject_id", updatable = false)
    @JsonIgnore
    private Subject subject;

    /**
     * Key of the user who created message.
     */
    @Column(name = "user_key", updatable = false)
    @JsonIgnore
    private String userKey = "";

    /**
     * Message's author.
     */
    @Transient
    private User user = new User();

    /**
     * Date when message was created.
     */
    @Column(name = "create_date", updatable = false)
    private Calendar createDate = Calendar.getInstance();

    /**
     * Message's text.
     */
    @Column(name = "message_text")
    private String text = "";

    /**
     * Constructs empty <code>Message</code> object.
     */
    public Message() {
    }

    /**
     * Get message's id.
     *
     * @return message's id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set message's id.
     *
     * @param id message's id.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Get subject in which message is contained.
     *
     * @return subject in which message is contained.
     */
    public Subject getSubject() {
        return this.subject;
    }

    /**
     * Set subject in which message is contained.
     *
     * @param subject subject in which message is contained.
     */
    public void setSubject(final Subject subject) {
        this.subject = subject;
    }

    /**
     * Get key of the user who created message.
     *
     * @return key of the user who created message.
     */
    public String getUserKey() {
        return this.userKey;
    }

    /**
     * Set key of the user who created message.
     *
     * @param userKey key of the user who created message.
     */
    public void setUserKey(final String userKey) {
        this.userKey = userKey;
    }

    /**
     * Get message's author.
     *
     * @return message's author.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Set message's author.
     *
     * @param user message's author.
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Get date when message was created.
     *
     * @return date when message was created.
     */
    public Calendar getCreateDate() {
        return this.createDate;
    }

    /**
     * Set date when message was created.
     *
     * @param createDate date when message was created.
     */
    public void setCreateDate(final Calendar createDate) {
        this.createDate = createDate;
    }

    /**
     * Get message's text.
     *
     * @return message's text.
     */
    public String getText() {
        return this.text;
    }

    /**
     * Set message's text.
     *
     * @param text message's text.
     */
    public void setText(final String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return id == message.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}