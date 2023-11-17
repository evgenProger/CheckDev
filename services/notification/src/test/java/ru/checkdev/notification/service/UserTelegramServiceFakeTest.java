package ru.checkdev.notification.service;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.domain.UserTelegram;
import ru.checkdev.notification.repository.SubscribeTopicRepositoryFake;
import ru.checkdev.notification.repository.UserTelegramRepositoryFake;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTelegramServiceFakeTest {
    @Test
    void save() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake()));
        var chatId = 333L;
        service.save(new UserTelegram(11, 10, chatId));
        var userTg = service.findByChatId(chatId);
        assertThat(userTg.isPresent()).isTrue();
        assertThat(userTg.get().getUserId()).isEqualTo(10);
    }

    @Test
    void whenfindByChatIdThenReturnOptionalEmpty() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake()));
        var chatId = 333L;
        var actual = service.findByChatId(chatId);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenfindByChatIdThenReturnOptionalUserID() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake()));
        var chatId = 333L;
        var userTg = new UserTelegram(11, 10, chatId);
        service.save(userTg);
        var actual = service.findByChatId(chatId);
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(userTg);
    }

    @Test
    void whenFindByUserIdThenOptionalEmpty() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake()));
        var actual = service.findByUserId(-1);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindByUserIdThenOptionalUserTg() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake()));
        var chatId = 333L;
        var userTg = new UserTelegram(11, 10, chatId);
        service.save(userTg);
        var actual = service.findByUserId(userTg.getUserId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(userTg);
    }

    @Test
    void whenFindAllByTopicIdAndUserIdNotWhenEmptyList() {
        var service = new UserTelegramService(new UserTelegramRepositoryFake(new SubscribeTopicRepositoryFake()));
        var actual = service.findAllByTopicIdAndUserIdNot(-1, -1);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void whenFindAllByTopicIdAndUserIdNotWhenList() {
        var topicSubFake = new SubscribeTopicRepositoryFake();
        var userTgFake = new UserTelegramRepositoryFake(topicSubFake);
        var user1 = new UserTelegram(0, 1, 1111L);
        var user2 = new UserTelegram(0, 2, 2222L);
        var user3 = new UserTelegram(0, 3, 3333L);
        userTgFake.save(user1);
        userTgFake.save(user2);
        userTgFake.save(user3);
        var topicSub1 = new SubscribeTopic(0, user1.getUserId(), 22);
        var topicSub2 = new SubscribeTopic(0, user2.getUserId(), 22);
        var topicSub3 = new SubscribeTopic(0, user3.getUserId(), 22);
        topicSubFake.save(topicSub1);
        topicSubFake.save(topicSub2);
        topicSubFake.save(topicSub3);
        var service = new UserTelegramService(userTgFake);
        var expect = List.of(user1, user2, user3);
        var actual = service.findAllByTopicIdAndUserIdNot(topicSub1.getTopicId(), -1);
        assertThat(actual).isEqualTo(expect);
    }
}