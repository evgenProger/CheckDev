package ru.checkdev.desc.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.checkdev.desc.domain.Topic;
import ru.checkdev.desc.dto.TopicLiteDTO;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends CrudRepository<Topic, Integer> {
    List<Topic> findAllByOrderByPositionAsc();

    List<Topic> findByCategoryIdOrderByPositionAsc(Integer categoryId);

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Modifying
    @Query("update cd_topic t set t.total = t.total + 1 where t.id=:id")
    void incrementTotal(@Param("id") int id);

    @Query("SELECT t.name FROM cd_topic t WHERE t.id = :id")
    Optional<String> getNameById(@Param("id") int id);

    /**
     * Метод собирает список всех topic в модель TopicLiteDTO
     *
     * @return List<TopicLiteDTO>
     */
    @Query("""
            SELECT 
            new ru.checkdev.desc.dto.TopicLiteDTO(ct.id, ct.name, ct.text, cc.id, cc.name, ct.position) 
            FROM cd_category cc 
            JOIN cd_topic ct ON cc.id = ct.category.id
            """)
    List<TopicLiteDTO> getAllTopicLiteDTO();
}
