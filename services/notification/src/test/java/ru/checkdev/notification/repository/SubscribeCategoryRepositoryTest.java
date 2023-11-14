package ru.checkdev.notification.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.checkdev.notification.domain.SubscribeCategory;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest()
@ExtendWith(SpringExtension.class)
public class SubscribeCategoryRepositoryTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SubscribeCategoryRepository repository;

    private SubscribeCategory subscribeCategory;

    @BeforeEach
    void init() {
        subscribeCategory = new SubscribeCategory(0, 1, 1);
        entityManager.createQuery("DELETE FROM cd_subscribe_category").executeUpdate();
        entityManager.persist(subscribeCategory);
        entityManager.clear();
    }

    @Test
    void checkThatStuffNotNull() {
        assertNotNull(entityManager);
        assertNotNull(repository);
    }

    @Test
    void whenFindSubscribeCategoryByUserId() {

        var result = repository.findByUserId(1);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertThat(result.get(0)).isEqualTo(subscribeCategory);
    }

    @Test
    void whenSubscribeCategoryNotFoundByUserId() {
        assertTrue(repository.findByUserId(2).isEmpty());
    }

    @Test
    void whenFindByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndCategoryId(1, 1);
        assertNotNull(result);
        assertThat(result).isEqualTo(subscribeCategory);
    }

    @Test
    void whenNotFoundByUserIdAndCategoryId() {
        var result = repository.findByUserIdAndCategoryId(2, 2);
        assertNull(result);
    }

    @Test
    void whenFindUserIdByCategoryId() {
        var result = repository.findUserIdByCategoryId(1);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertThat(result.get(0)).isEqualTo(1);
    }

    @Test
    void whenUserIdNotFoundByCategoryId() {
        var result = repository.findUserIdByCategoryId(2);
        assertTrue(result.isEmpty());
    }
}
