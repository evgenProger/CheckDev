package ru.checkdev.mock.repository;

import org.springframework.data.repository.CrudRepository;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import java.util.List;

public interface WisherRepository extends CrudRepository<Wisher, Integer> {

    List<Wisher> findByInterview(Interview interview);

    List<Wisher> findAll();
}
