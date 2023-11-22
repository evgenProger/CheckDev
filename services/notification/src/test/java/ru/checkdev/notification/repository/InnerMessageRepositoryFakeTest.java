package ru.checkdev.notification.repository;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.InnerMessage;
import ru.checkdev.notification.dto.InnerMessageDTO;

import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InnerMessageRepositoryFakeTest {

    private InnerMessageRepositoryFake innerMessageRepository = new InnerMessageRepositoryFake();

    @Test
    void whenFindByUserIdAndReadFalseWhenOneOfInterviewsHasReadTrue() {
        var message1 = new InnerMessage(1, 12, "message_1",
                new Timestamp(System.currentTimeMillis()), false, 1);
        var message2 = new InnerMessage(2, 12, "message_2",
                new Timestamp(System.currentTimeMillis()), true, 2);
        var message3 = new InnerMessage(3, 12, "message_3",
                new Timestamp(System.currentTimeMillis()), false, 3);
        innerMessageRepository.memory.put(1, message1);
        innerMessageRepository.memory.put(2, message2);
        innerMessageRepository.memory.put(3, message3);
        assertThat(innerMessageRepository.findByUserIdAndReadFalse(12))
                .isEqualTo(List.of(message1, message3));
    }

    @Test
    void whenFindByUserIdAndReadFalseWhenOneOfUserIdsDifferent() {
        var message1 = new InnerMessage(1, 12, "message_1",
                new Timestamp(System.currentTimeMillis()), false, 1);
        var message2 = new InnerMessage(2, 12, "message_2",
                new Timestamp(System.currentTimeMillis()), false, 2);
        var message3 = new InnerMessage(3, 13, "message_3",
                new Timestamp(System.currentTimeMillis()), false, 3);
        innerMessageRepository.memory.put(1, message1);
        innerMessageRepository.memory.put(2, message2);
        innerMessageRepository.memory.put(3, message3);
        assertThat(innerMessageRepository.findByUserIdAndReadFalse(12))
                .isEqualTo(List.of(message1, message2));
    }

    @Test
    void whenNothingFoundByUserIdAndReadFalse() {
        var message1 = new InnerMessage(1, 12, "message_1",
                new Timestamp(System.currentTimeMillis()), false, 1);
        var message2 = new InnerMessage(2, 12, "message_2",
                new Timestamp(System.currentTimeMillis()), false, 2);
        var message3 = new InnerMessage(3, 13, "message_3",
                new Timestamp(System.currentTimeMillis()), false, 3);
        innerMessageRepository.memory.put(1, message1);
        innerMessageRepository.memory.put(2, message2);
        innerMessageRepository.memory.put(3, message3);
        assertThat(innerMessageRepository.findByUserIdAndReadFalse(9))
                .isEqualTo(List.of());
    }

    @Test
    void whenFindDTOByUserIdAndReadFalseWhenOneOfInterviewsHasReadTrue() {
        var message1 = new InnerMessage(1, 12, "message_1",
                new Timestamp(System.currentTimeMillis()), false, 1);
        var message2 = new InnerMessage(2, 12, "message_2",
                new Timestamp(System.currentTimeMillis()), true, 2);
        var message3 = new InnerMessage(3, 12, "message_3",
                new Timestamp(System.currentTimeMillis()), false, 3);

        var messageDTO1 = new InnerMessageDTO(1, 12, "message_1",
                message1.getCreated(), 1);
        var messageDTO3 = new InnerMessageDTO(3, 12, "message_3",
                message3.getCreated(), 3);
        innerMessageRepository.memory.put(1, message1);
        innerMessageRepository.memory.put(2, message2);
        innerMessageRepository.memory.put(3, message3);
        assertThat(innerMessageRepository.findMessageDTOByUserIdAndReadFalse(12))
                .isEqualTo(List.of(messageDTO1, messageDTO3));
    }

    @Test
    void whenFindDTOByUserIdAndReadFalseWhenOneOfUserIdsDifferent() {
        var message1 = new InnerMessage(1, 12, "message_1",
                new Timestamp(System.currentTimeMillis()), false, 1);
        var message2 = new InnerMessage(2, 12, "message_2",
                new Timestamp(System.currentTimeMillis()), false, 2);
        var message3 = new InnerMessage(3, 13, "message_3",
                new Timestamp(System.currentTimeMillis()), false, 3);

        var messageDTO1 = new InnerMessageDTO(1, 12, "message_1",
                message1.getCreated(), 1);
        var messageDTO2 = new InnerMessageDTO(2, 12, "message_2",
                message3.getCreated(), 2);
        innerMessageRepository.memory.put(1, message1);
        innerMessageRepository.memory.put(2, message2);
        innerMessageRepository.memory.put(3, message3);
        assertThat(innerMessageRepository.findMessageDTOByUserIdAndReadFalse(12))
                .isEqualTo(List.of(messageDTO1, messageDTO2));
    }

    @Test
    void whenNoOneDTOFoundByUserIdAndReadFalse() {
        var message1 = new InnerMessage(1, 12, "message_1",
                new Timestamp(System.currentTimeMillis()), false, 1);
        var message2 = new InnerMessage(2, 12, "message_2",
                new Timestamp(System.currentTimeMillis()), false, 2);
        var message3 = new InnerMessage(3, 13, "message_3",
                new Timestamp(System.currentTimeMillis()), false, 3);
        innerMessageRepository.memory.put(1, message1);
        innerMessageRepository.memory.put(2, message2);
        innerMessageRepository.memory.put(3, message3);
        assertThat(innerMessageRepository.findMessageDTOByUserIdAndReadFalse(27))
                .isEqualTo(List.of());
    }

}
