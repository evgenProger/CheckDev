package ru.checkdev.forum.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * User model, received from <code>auth</code> microservice.
 * Used as subject and message author.
 *
 * @author LightStar
 * @since 08.06.2017
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class User implements Serializable {

    /**
     * User's id.
     */
    private int id = -1;

    /**
     * User's name.
     */
    @JsonProperty("username")
    private String name = "<DELETED>";

    /**
     * User's key.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String key = "";

    /**
     * User's email.
     */
    private String email = "";

    /**
     * Get user's id.
     *
     * @return user's id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set user's id.
     * @param id user's id.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Get user's name.
     *
     * @return user's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set user's name.
     *
     * @param name user's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get user's key.
     *
     * @return user's key.
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Set user's key.
     *
     * @param key user's key.
     */
    public void setKey(final String key) {
        this.key = key;
    }

    /**
     * Get user's email.
     *
     * @return user's email.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Set user's email.
     *
     * @param email user's email.
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}