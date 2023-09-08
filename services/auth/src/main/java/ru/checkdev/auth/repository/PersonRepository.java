package ru.checkdev.auth.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.checkdev.auth.domain.Person;
import ru.checkdev.auth.domain.Photo;

import java.util.List;

/**
 *
 * @author parsentev
 * @since 25.09.2016
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {

    Person findByEmail(String email);

    Person findByEmailAndUsername(String email, String username);

    Person findByKey(String key);

    List<Person> findByKeyIn(List<String> key);

    @Modifying
    @Query("update person p set p.username = ?1, p.password = ?2, p.experience = ?3, p.about = ?4, p.aboutShort = ?5, p.show = ?6, p.salary=?7,  p.location=?8 where p.email = ?9")
    int update(String username, String password, String experience, String about, String aboutShort, boolean show, String salary, String location, String email);

    @Modifying
    @Query("update person p set p.username = ?1, p.password = ?2, p.experience = ?3, p.about = ?4, p.aboutShort = ?5, p.photo =?6 where p.email = ?7")
    int updateWithImg(String username, String password, String experience, String about, String aboutShort, Photo photo, String email);

    @Query("select count(p.id) from person as p")
    Long total();

    Page findAll(Pageable pageable);

    Page findByEmailContainingOrUsernameContaining(String email, String userName, Pageable pageable);

    @Query("select new ru.checkdev.auth.domain.Person(p.key, p.experience, p.salary, p.aboutShort, p.username, p.location, p.photo.id) from person as p left JOIN p.photo where p.show is ?1 order by p.updated desc")
    List<Person> findByShow(boolean show, Pageable pageable);

    @Query("select new ru.checkdev.auth.domain.Person(p.key, p.experience, p.salary, p.aboutShort, p.username, p.location, p.photo.id) from person as p left JOIN p.photo where p.show is ?1 order by p.updated desc")
    List<Person> findByShow(boolean show);

    @Query("select count(p.id) from person as p where p.show is true")
    Long showed();

    @Query("select p from person p left join  p.photo where p.email = :email")
    Person findPerson(@Param("email") String email);
}
