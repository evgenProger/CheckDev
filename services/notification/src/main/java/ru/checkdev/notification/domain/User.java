package ru.checkdev.notification.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private int id;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "cd_subscribe_category",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "category_id") }
    )
    private List<Category> subscribeCategoriesList = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Category> getSubscribeCategoriesList() {
        return subscribeCategoriesList;
    }

    public void setSubscribeCategoriesList(List<Category> subscribeCategoriesList) {
        this.subscribeCategoriesList = subscribeCategoriesList;
    }
}