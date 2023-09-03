package ru.job4j.auth.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.domain.Role;
import ru.job4j.auth.repository.PersonRepository;

import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author parsentev
 * @since 21.09.2016
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleServiceTest {
    @Autowired
    private RoleService service;

    @Autowired
    private PersonRepository persons;

    @Test
    public void whenAddRolesThenPersonHasRoles() {
        Role role = this.service.save(new Role("ROLE_ADMIN"));
        Person person = new Person("Петр Арсентьев", String.format("%s@yandex.ru", System.currentTimeMillis()), "password");
        person.setRoles(Collections.singletonList(role));
        this.persons.save(person);
        Person result = this.persons.findByEmail(person.getEmail());
        assertThat(result.getRoles().isEmpty(), is(false));
    }
}