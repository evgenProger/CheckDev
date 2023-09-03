package ru.job4j.forum.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Forum category model.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@Entity(name = "category")
public class Category implements Serializable {

    /**
     * Category's id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id = -1;

    /**
     * Category's sorting position.
     */
    @Column(name = "position")
    private int position = 0;

    /**
     * Category's name.
     */
    @Column(name = "category_name", unique = true)
    private String name = "";

    /**
     * Category's description.
     */
    @Column(name = "category_desc")
    private String description = "";

    public Category() {
    }

    public Category(int id) {
        this.id = id;
    }

    /**
     * Get category's id.
     *
     * @return category's id.
     */
    public int getId() {
        return this.id;
    }

    /**
     * Set category's id.
     *
     * @param id category's id.
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Get category's sorting position.
     *
     * @return category's sorting position.
     */
    public int getPosition() {
        return this.position;
    }

    /**
     * Set category's position.
     *
     * @param position category's sorting position.
     */
    public void setPosition(final int position) {
        this.position = position;
    }

    /**
     * Get category's name.
     *
     * @return category's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set category's name.
     *
     * @param name category's name.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Get category's description.
     *
     * @return category's description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set category's description.
     *
     * @param description category's description.
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return id == category.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}