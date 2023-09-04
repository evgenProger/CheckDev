package ru.checkdev.exam.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.exam.domain.Exam;

import java.util.List;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public interface ExamRepository extends CrudRepository<Exam, Integer> {
    List<Exam> findByActiveOrderByPosition(boolean active);

    Exam findByIdAndActive(int id, boolean active);
}
