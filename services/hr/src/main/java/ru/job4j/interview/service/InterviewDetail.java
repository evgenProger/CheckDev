package ru.job4j.interview.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.job4j.interview.domain.ITask;
import ru.job4j.interview.domain.Interview;
import ru.job4j.interview.domain.Track;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class InterviewDetail {
    private Interview interview;
    private Person person;
    private final List<Track> track;
    private final Map<Integer, ITask> tasks;

    public InterviewDetail(Interview interview, Person person, List<Track> track, Map<Integer, ITask> tasks) {
        this.interview = interview;
        this.person = person;
        this.track = track;
        this.tasks = tasks;
    }

    public Map<Integer, ITask> getTasks() {
        return tasks;
    }

    public List<Track> getTrack() {
        return track;
    }

    public Interview getInterview() {
        return interview;
    }

    public Person getPerson() {
        return person;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Person {
        private int id;
        private String username;
        private String email;
        private String key;
        private boolean active;
        private List<Role> roles;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Role {
        private int id;
        private String value;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
