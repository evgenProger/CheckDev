package ru.job4j.forum.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Forum subject model.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@Entity(name = "subject")
public class Subject implements Serializable {

    /**
     * Subject's id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private int id = -1;

    /**
     * Category in which subject is contained.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id", updatable = false)
    @JsonIgnore
    private Category category;

    /**
     * Key of the user who created subject.
     */
    @Column(name="user_key", updatable = false)
    @JsonIgnore
    private String userKey = "";

    /**
     * Subject author.
     */
    @Transient
    private User user = new User();

    /**
     * Date when subject was created.
     */
    @Column(name = "create_date", updatable = false)
    private Calendar createDate = Calendar.getInstance();

    /**
     * Subject's name.
     */
    @Column(name = "subject_name")
    private String name = "";

    /**
     * Subject's description.
     */
    @Column(name = "description")
    private String description = "";

    /**
     * Subject's brief description.
     */
    @Column(name = "brief")
    private String brief;

    /**
     * Key of the user who added last message to subject.
     */
    @Column(name="last_user_key")
    @JsonIgnore
    private String lastUserKey = "";

    /**
     * User who added last message to subject.
     */
    @Transient
    private User lastUser = new User();

    /**
     * Date when last message was added to subject.
     */
    @Column(name = "last_date")
    private Calendar lastDate = Calendar.getInstance();

    /**
     * Count of messages in subject.
     */
    @Column(name = "count_message")
    private int countMessage = 0;

    /**
     * Count of views of subject page.
     */
    @Column(name = "count_view")
    private int countView = 0;

    public Subject() {
    }

    public Subject(final int id) {
        this.id = id;
    }

    /**
     * Get subject's id.
     *
     * @return subject's id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set subject's id.
     *
     * @param id subject's id.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Set category in which subject is contained.
     *
     * @return category in which subject is contained.
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Set category in which subject is contained.
     *
     * @param category category in which subject is contained.
     */
    public void setCategory(final Category category) {
        this.category = category;
    }

    /**
     * Get key of the user who created subject.
     *
     * @return key of the user who created subject.
     */
    public String getUserKey() {
        return this.userKey;
    }

    /**
     * Set key of the user who created subject.
     *
     * @param userKey key of the user who created subject.
     */
    public void setUserKey(final String userKey) {
        this.userKey = userKey;
    }

    /**
     * Get subject's author.
     *
     * @return subject's author.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Set subject's author.
     *
     * @param user subject's author.
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Get date when subject was created.
     *
     * @return date when subject was created.
     */
    public Calendar getCreateDate() {
        return this.createDate;
    }

    /**
     * Set date when subject was created.
     *
     * @param createDate date when subject was created.
     */
    public void setCreateDate(final Calendar createDate) {
        this.createDate = createDate;
    }

    /**
     * Get subject's name.
     *
     * @return subject's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set subject's name.
     *
     * @param name subject's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get subject's description.
     *
     * @return subject's description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set subject's description.
     *
     * @param description subject's description.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    public String getBrief() {
        return this.brief;
    }

    public void setBrief(final String brief) {
        this.brief = brief;
    }

    public String getLastUserKey() {
        return this.lastUserKey;
    }

    public void setLastUserKey(final String lastUserKey) {
        this.lastUserKey = lastUserKey;
    }

    public User getLastUser() {
        return this.lastUser;
    }

    public void setLastUser(final User lastUser) {
        this.lastUser = lastUser;
    }

    public Calendar getLastDate() {
        return this.lastDate;
    }

    public void setLastDate(final Calendar lastDate) {
        this.lastDate = lastDate;
    }

    public int getCountMessage() {
        return this.countMessage;
    }

    public void setCountMessage(final int countMessage) {
        this.countMessage = countMessage;
    }

    public int getCountView() {
        return this.countView;
    }

    public void setCountView(final int countView) {
        this.countView = countView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return id == subject.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}