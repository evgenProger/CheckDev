package ru.checkdev.notification.repository;

import org.junit.jupiter.api.Test;
import ru.checkdev.notification.domain.SubscribeTopic;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CheckDev пробное собеседование
 * UserTelegramRepository TEST
 *
 * @author Dmitry Stepanov
 * @version 18.11.2023 00:46
 */
class UserTelegramRepositoryTest {
    private final SubscribeTopicRepositoryFake topicFake = new SubscribeTopicRepositoryFake();
    private final UserTelegramRepositoryFake userTelegramFake = new UserTelegramRepositoryFake(topicFake);


    @Test
    void whenFindByChatIdThenReturnOptionalEmpty() {
        var actual = userTelegramFake.findByChatId(-1);
        assertThat(actual.isEmpty()).isTrue();
    }

    @Test
    void whenFindByChatIdThenReturnOptionalUserTelegram() {
        var userTelegram = new UserTelegram(0, 1, 1111L);
        userTelegramFake.save(userTelegram);
        var actual = userTelegramFake.findByChatId(userTelegram.getChatId());
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get()).isEqualTo(userTelegram);
    }

    @Test
    void whenFindChatIdInUserIdsWhenEmptyList() {
        var actual = userTelegramFake.findChatIdInUserIds(List.of(1, 2, 3));
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindChatIdByUserId() {
        var user = new UserTelegram(0, 1, 1111L);
        userTelegramFake.save(user);
        assertThat(Optional.of(1111L)).isEqualTo(userTelegramFake.findChatIdByUserId(1));
    }

    @Test
    void whenTryToFindChatIdByInvalidUserId() {
        var user = new UserTelegram(0, 1, 1111L);
        userTelegramFake.save(user);
        assertThat(Optional.empty()).isEqualTo(userTelegramFake.findChatIdByUserId(27));
    }

    @Test
    void whenFindChatIdInUserIdsWhenListChatID() {
        var user1 = new UserTelegram(0, 1, 1111L);
        var user2 = new UserTelegram(0, 2, 2222L);
        var user3 = new UserTelegram(0, 3, 3333L);
        userTelegramFake.save(user1);
        userTelegramFake.save(user2);
        userTelegramFake.save(user3);
        List<Integer> userIds = List.of(user1.getUserId(), user2.getUserId(), user3.getUserId());
        List<Long> expect = List.of(user1.getChatId(), user2.getChatId(), user3.getChatId());
        var actual = userTelegramFake.findChatIdInUserIds(userIds);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenFindChatIdInUserIdsWhenListChatIDFirst() {
        var user1 = new UserTelegram(0, 1, 1111L);
        var user2 = new UserTelegram(0, 2, 2222L);
        var user3 = new UserTelegram(0, 3, 3333L);
        userTelegramFake.save(user1);
        userTelegramFake.save(user2);
        userTelegramFake.save(user3);
        List<Integer> userIds = List.of(user1.getUserId());
        List<Long> expect = List.of(user1.getChatId());
        var actual = userTelegramFake.findChatIdInUserIds(userIds);
        assertThat(actual).isEqualTo(expect);
    }


    @Test
    void whenFindByUserIdThenOptionalEmpty() {
        var actual = userTelegramFake.findByUserId(-1);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindByUserIdThenOptionalUserTelegram() {
        var user1 = new UserTelegram(0, 1, 1111L);
        userTelegramFake.save(user1);
        var actual = userTelegramFake.findByUserId(user1.getUserId());
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(user1);
    }

    @Test
    void whenFindAllByTopicIdAndUserIdNotWhenEmptyList() {
        var actual = userTelegramFake.findAllByTopicIdAndUserIdNot(-1, -1);
        assertThat(actual).isEmpty();
    }

    @Test
    void whenFindAllByTopicIdAndUserIdNotWhenListAllUser() {
        var user1 = new UserTelegram(0, 1, 1111L);
        var user2 = new UserTelegram(0, 2, 2222L);
        var user3 = new UserTelegram(0, 3, 3333L);
        userTelegramFake.save(user1);
        userTelegramFake.save(user2);
        userTelegramFake.save(user3);
        var topicSub1 = new SubscribeTopic(0, user1.getUserId(), 22);
        var topicSub2 = new SubscribeTopic(0, user2.getUserId(), 22);
        var topicSub3 = new SubscribeTopic(0, user3.getUserId(), 22);
        topicFake.save(topicSub1);
        topicFake.save(topicSub2);
        topicFake.save(topicSub3);
        var expect = List.of(user1, user2, user3);
        var actual = userTelegramFake.findAllByTopicIdAndUserIdNot(topicSub1.getTopicId(), -1);
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void whenFindAllByTopicIdAndUserIdNotWhenListNoFirstUser() {
        var user1 = new UserTelegram(0, 1, 1111L);
        var user2 = new UserTelegram(0, 2, 2222L);
        var user3 = new UserTelegram(0, 3, 3333L);
        userTelegramFake.save(user1);
        userTelegramFake.save(user2);
        userTelegramFake.save(user3);
        var topicSub1 = new SubscribeTopic(0, user1.getUserId(), 22);
        var topicSub2 = new SubscribeTopic(0, user2.getUserId(), 22);
        var topicSub3 = new SubscribeTopic(0, user3.getUserId(), 22);
        topicFake.save(topicSub1);
        topicFake.save(topicSub2);
        topicFake.save(topicSub3);
        var expect = List.of(user2, user3);
        var actual = userTelegramFake.findAllByTopicIdAndUserIdNot(topicSub1.getTopicId(), user1.getUserId());
        assertThat(actual).isEqualTo(expect);
    }
}