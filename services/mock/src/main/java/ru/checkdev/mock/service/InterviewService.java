package ru.checkdev.mock.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.checkdev.mock.domain.Interview;
import ru.checkdev.mock.repository.InterviewRepository;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;

    private static final Logger LOG = LoggerFactory.getLogger(InterviewService.class.getName());

    public Optional<Interview> save(Interview interview) {
        Optional<Interview> rsl = Optional.empty();
        try {
            rsl = Optional.of(interviewRepository.save(interview));
        } catch (DataIntegrityViolationException e) {
            LOG.error("Error!", e);
        }
        return rsl;
    }

    public List<Interview> findAll() {
        return interviewRepository.findAll();
    }

    public Optional<Interview> findById(Integer id) {
        return interviewRepository.findById(id);
    }

    public boolean update(Interview interview) {
        interviewRepository.save(interview);
        return true;
    }

    public boolean delete(Interview interview) {
        if (findById(interview.getId()).isPresent()) {
            interviewRepository.delete(interview);
            return true;
        }
        return false;
    }
}
