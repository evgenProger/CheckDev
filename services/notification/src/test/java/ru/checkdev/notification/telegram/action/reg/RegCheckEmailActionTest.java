package ru.checkdev.notification.telegram.action.reg;

import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
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
class RegCheckEmailActionTest {
    private final SessionTg sessionTg = new SessionTg();

    @Test
    void whenRegCheckEmailActionEmailNotValidThenReturnMessageEmailIncorrect() {
        Update update = new Update();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("email.ru");
        update.setMessage(message);
        sessionTg.put(String.valueOf(chat.getId()), "email", message.getText());
        RegCheckEmailAction regCheckEmailAction = new RegCheckEmailAction(sessionTg);
        SendMessage sendMessage = (SendMessage) regCheckEmailAction.handle(update).get();
        String actual = sendMessage.getText();
        String ls = System.lineSeparator();
        String expect = new StringBuilder().append("Email: ")
                .append(message.getText())
                .append(" не корректный.").append(ls)
                .append("попробуйте снова.").append(ls)
                .append("/new").toString();
        assertThat(actual).isEqualTo(expect);
    }


    @Test
    void whenRegCheckEmailActionEmailCorrectThenReturnEmptyMessage() {
        Update update = new Update();
        Chat chat = new Chat(1L, "type");
        Message message = new Message();
        message.setChat(chat);
        message.setText("email@email.ru");
        update.setMessage(message);
        sessionTg.put(String.valueOf(chat.getId()), "email", message.getText());
        RegCheckEmailAction regCheckEmailAction = new RegCheckEmailAction(sessionTg);
        Optional<BotApiMethod> botApiMessage = regCheckEmailAction.handle(update);
        assertThat(botApiMessage).isEmpty();
    }
}