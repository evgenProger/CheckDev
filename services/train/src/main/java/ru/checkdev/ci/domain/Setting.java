package ru.checkdev.ci.domain;

import javax.persistence.*;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "setting")
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String value;

    @Enumerated(EnumType.ORDINAL)
    private Type type = Type.WORKSPACE;

    public Setting() {
    }

    public Setting(String value, Type type) {
        this.value = value;
        this.type = type;
    }

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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Setting setting = (Setting) o;

        return id == setting.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public enum Type {
        WORKSPACE, MVN_HOME, GIT_USER, GIT_PWD
    }
}
