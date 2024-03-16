package ru.checkdev.generator.service;

import java.util.Collection;

public interface ExamService<T, K> {

    public Collection<T> create(Collection<K> keys);
}
