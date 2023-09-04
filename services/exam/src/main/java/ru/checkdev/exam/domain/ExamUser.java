package ru.checkdev.exam.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

/**
 * create table exam_user (
 * id serial not null primary key,
 * exam_id int not null references exam(id),
 * result int not null default 0,
 * start timestamp not null default now(),
 * finish timestamp,
 * user_key varchar(2000) not null
 * );
 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "exam_user")
public class ExamUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "exam_id", updatable = false)
    private Exam exam;

    private int result;

    private Calendar start;

    private Calendar finish;

    private String key;

    private int total;

    public ExamUser() {
    }

    public ExamUser(String key) {
        this.key = key;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getFinish() {
        return finish;
    }

    public void setFinish(Calendar finish) {
        this.finish = finish;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExamUser examUser = (ExamUser) o;

        return id == examUser.id;
    }

    @Override
    public int hashCode() {
        return id;
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