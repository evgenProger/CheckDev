package ru.job4j.forum.domain;

import java.util.Map;

/**
 * Notification model. It contains all data needed to send one notification to specific email.
 *
 * @author LightStar
 * @since 21.06.2017
 */
public class Notify {

    private String email;

    private Map<String, String> keys;

    private String template;

    public Notify(final String email, final Map<String, String> keys, final String template) {
        this.email = email;
        this.keys = keys;
        this.template = template;
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(final String template) {
        this.template = template;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Map<String, String> getKeys() {
        return this.keys;
    }

    public void setKeys(final Map<String, String> keys) {
        this.keys = keys;
    }

    public enum Type {
        FORUM_MESSAGE
    }
}