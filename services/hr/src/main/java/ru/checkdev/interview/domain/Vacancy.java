package ru.checkdev.interview.domain;

import javax.persistence.*;
import java.util.Calendar;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Entity(name = "vacancy")
public class Vacancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String location;

    private String rate;

    private Calendar published;

    private boolean active;

    private String description;

    private String company;

    private String experience;

    private Calendar created;

    @Column(name = "limit_time")
    private Integer limit;

    /**
     * User owner.
     */
    private String key;

    private boolean archive;

    private String congratulate;

    private String failure;

    private String intro;

    public Vacancy() {
    }

    public Vacancy(int id) {
        this.id = id;
    }

    public Vacancy(int id, String name, String location, String rate, Calendar published,
                   boolean active, String description, String company, String experience, Calendar created, String key, Integer limit, boolean arh) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.rate = rate;
        this.published = published;
        this.active = active;
        this.description = description;
        this.company = company;
        this.experience = experience;
        this.created = created;
        this.key = key;
        this.limit = limit;
        this.active = arh;
    }

    public Vacancy(String name, String location, String rate, String description,
                   String company, String experience, Calendar created, Integer limit, String key) {
        this.name = name;
        this.location = location;
        this.rate = rate;
        this.description = description;
        this.company = company;
        this.experience = experience;
        this.created = created;
        this.limit = limit;
        this.key = key;
    }


    public Vacancy(int id, String name, Calendar published, String company, String location, String rate,  String experience) {
       this.id = id;
        this.name = name;
        this.location = location;
        this.rate = rate;
        this.published = published;
        this.company = company;
        this.experience = experience;
    }
    public Vacancy(int id, String description, String name, Calendar published, String company, String location, String rate,  String experience) {
       this.id = id;
       this.description = description;
        this.name = name;
        this.location = location;
        this.rate = rate;
        this.published = published;
        this.company = company;
        this.experience = experience;
    }

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

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vacancy vacancy = (Vacancy) o;

        return id == vacancy.id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Calendar getPublished() {
        return published;
    }

    public void setPublished(Calendar published) {
        this.published = published;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }


    public Vacancy createClone() {
       return new Vacancy(this.name, this.location, this.rate, this.description,  this.company,
               this.experience, this.created, this.limit, this.key);
    }

    public String getCongratulate() {
        return congratulate;
    }

    public void setCongratulate(String congratulate) {
        this.congratulate = congratulate;
    }

    public String getFailure() {
        return failure;
    }

    public void setFailure(String failure) {
        this.failure = failure;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
