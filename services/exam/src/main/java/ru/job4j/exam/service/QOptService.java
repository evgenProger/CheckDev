package ru.job4j.exam.service;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.job4j.exam.domain.QOpt;
import ru.job4j.exam.repository.QOptRepository;

import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class QOptService {
    private final QOptRepository options;

    @Autowired
    public QOptService(final QOptRepository options) {
        this.options = options;
    }

    public QOpt save(QOpt option) {
        return this.options.save(option);
    }

    public List<QOpt> findAll() {
        return Lists.newArrayList(this.options.findAll());
    }

    public boolean delete(int id) {
        this.options.deleteById(id);
        return true;
    }

    public QOpt findById(int id) {
        return this.options.findById(id).get();
    }

    public List<QOpt> findByQuestionId(int questionId) {
        return this.options.findByQuestionIdOrderByPosAsc(questionId);
    }
}
