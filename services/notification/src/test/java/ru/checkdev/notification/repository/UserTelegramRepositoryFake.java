package ru.checkdev.notification.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import ru.checkdev.notification.domain.UserTelegram;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class UserTelegramRepositoryFake implements UserTelegramRepository{
    @Override
    public Optional<UserTelegram> findByChatId(long chatId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserTelegram> findByUserId(int userId) {
        return Optional.empty();
    }

    @Override
    public List<UserTelegram> findAll() {
        return null;
    }

    @Override
    public List<UserTelegram> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<UserTelegram> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserTelegram> findAllById(Iterable<Integer> integers) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(UserTelegram entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserTelegram> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends UserTelegram> S save(S entity) {
        return null;
    }

    @Override
    public <S extends UserTelegram> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<UserTelegram> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends UserTelegram> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends UserTelegram> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<UserTelegram> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public UserTelegram getOne(Integer integer) {
        return null;
    }

    @Override
    public UserTelegram getById(Integer integer) {
        return null;
    }

    @Override
    public UserTelegram getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends UserTelegram> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserTelegram> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserTelegram> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends UserTelegram> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserTelegram> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserTelegram> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends UserTelegram, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}
