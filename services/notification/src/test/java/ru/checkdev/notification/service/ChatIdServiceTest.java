package ru.checkdev.notification.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.ChatId;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations="classpath:application.properties")
@SpringBootTest(classes = NtfSrv.class)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
class ChatIdServiceTest {
    @Autowired
    private ChatIdService service;

    @Test
    void save() {
        this.service.save(new ChatId(1, "Arkadypar@mail.ru", new ArrayList<>()));
        assertEquals(this.service.findById(1).get().getEmail(), "Arkadypar@mail.ru");
    }

    @Test
    void whenRepeatSaveThanFalse() {
        this.service.save(new ChatId(2, "Par@mail.ru", new ArrayList<>()));
        Assertions.assertFalse(false, this.service.findById(2).get().getEmail());
    }
}