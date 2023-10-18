package ru.checkdev.notification.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.checkdev.notification.domain.User;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void saveAndFindById() {
        User user = new User(1, null);
        userService.save(user);
        User findUser = userService.findById(1).get();
        assertEquals(findUser.getId(), 1);
    }

    @Test
    public void findById() {
    }

    @Test
    public void saveSubscribeCategories() {
    }

    @Test
    public void deleteSubscribeCategories() {
    }

    @Test
    public void getUserDTOById() {
    }
}