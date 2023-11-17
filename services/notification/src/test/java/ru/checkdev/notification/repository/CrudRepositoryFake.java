package ru.checkdev.notification.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.notification.domain.Base;
import ru.checkdev.notification.domain.SubscribeTopic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CrudRepositoryFake<T extends Base> implements CrudRepository<T, Integer> {
    protected final Map<Integer, T> memory = new HashMap<>();
    private int gen = 1;

    @Override
    public <S extends T> S save(S entity) {
        entity.setId(gen++);
        memory.put(entity.getId(), entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        entities.forEach(this::save);
        return entities;
    }

    @Override
    public Optional<T> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer id) {
        return false;
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }

    @Override
    public Iterable<T> findAllById(Iterable<Integer> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void delete(T entity) {
        memory.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> ids) {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {
        memory.clear();
    }
}
