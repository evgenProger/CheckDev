package ru.checkdev.notification.telegram.action.reg;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.checkdev.notification.telegram.SessionTg;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 27.11.2023
 */
class RegPutEmailActionTest {
    private final SessionTg sessionTg = new SessionTg();

    @Test
    void whenPutEmailActionThenReturnSessionTgEmail() {
        RegPutEmailAction regPutEmailAction = new RegPutEmailAction(sessionTg);
        Update update = new Update();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("newEmailUser");
        update.setMessage(message);
        Optional<BotApiMethod> handleResult = regPutEmailAction.handle(update);
        var actual = sessionTg.get(String.valueOf(chat.getId()), "email", "");
        var expect = message.getText();
        assertThat(handleResult).isEmpty();
        assertThat(actual).isEqualTo(expect);
    }
}