package ru.checkdev.mock.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.domain.Wisher;
import ru.checkdev.mock.repository.WisherRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WisherService {

    private final WisherRepository wisherRepository;

    private static final Logger LOG = LoggerFactory.getLogger(WisherService.class.getName());

    public Optional<Wisher> save(Wisher wisher) {
        Optional<Wisher> rsl = Optional.empty();
        try {
            rsl = Optional.of(wisherRepository.save(wisher));
        } catch (DataIntegrityViolationException e) {
            LOG.error("Error!", e);
        }
        return rsl;
    }

    public List<Wisher> findByInterview(Interview interview) {
        return wisherRepository.findByInterview(interview);
    }

   public List<Wisher> findAll() {
        return wisherRepository.findAll();
   }
    public Optional<Wisher> findById(int id) {
        return wisherRepository.findById(id);
    }


    public boolean update(Wisher wisher) {
        wisherRepository.save(wisher);
        return true;
    }

    public boolean delete(Wisher wisher) {
        if (findById(wisher.getId()).isPresent()) {
            wisherRepository.delete(wisher);
            return true;
        }
        return false;
    }
}
