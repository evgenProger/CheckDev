package ru.checkdev.generator.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Key {

    @Id
    @Column(name = "id", nullable = false)
    private int id;
    private String value;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Key key)) {
            return false;
        }
        return id == key.id && value.equals(key.value);
    }

    @Override
    public int hashCode() {
        return (Objects.hash(id) * 31) + value.hashCode();
    }
}
