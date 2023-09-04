package ru.checkdev.interview.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Author : Pavel Ravvich.
 * Created : 12.08.17.
 *
 * Template of vacancy for stupid HRs.
 */
@Entity(name = "template_vacancy")
public class TemplateVacancy {
    /**
     * Primary key.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private String template;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJsonObj() {
        return template;
    }

    public void setJsonObj(String template) {
        this.template = template;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TemplateVacancy)) return false;

        TemplateVacancy templateVacancy = (TemplateVacancy) o;

        return id == templateVacancy.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
