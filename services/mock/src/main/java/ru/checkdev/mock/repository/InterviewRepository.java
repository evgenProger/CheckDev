package ru.checkdev.mock.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.mock.domain.Interview;
import java.util.List;
import java.util.Optional;

public interface InterviewRepository extends CrudRepository<Interview, Integer> {

    List<Interview> findAll();

    Optional<Interview> findById(int id);

}
