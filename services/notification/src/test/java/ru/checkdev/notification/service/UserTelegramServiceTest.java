package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.checkdev.notification.NtfSrv;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = NtfSrv.class)
@AutoConfigureMockMvc
class UserTelegramServiceTest {
    @MockBean
    private UserTelegramService service;

    @Test
    void save() {
        UserTelegram userTelegram = new UserTelegram(11, 10, 333L);
        when(this.service.save(userTelegram)).thenReturn(true);
        when(this.service.findByChatId(userTelegram.getChatId())).thenReturn(Optional.of(userTelegram));
        assertEquals(this.service.findByChatId(userTelegram.getChatId()).get().getUserId(), userTelegram.getUserId());
    }

    @Test
    void whenRepeatSaveThanFalse() {
        UserTelegram userTelegram = new UserTelegram(11, 10, 444L);
        when(this.service.save(userTelegram)).thenReturn(true);
        assertEquals(this.service.save(userTelegram), true);
    }
}