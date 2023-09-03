package ru.job4j.forum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.job4j.forum.domain.Subject;
import ru.job4j.forum.domain.User;
import ru.job4j.forum.repository.SubjectRepository;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Map;

/**
 * Implementation of {@link SubjectService} interface.
 *
 * @author LightStar
 * @since 01.06.2017
 */
@Service
public class SubjectServiceImpl implements SubjectService {

    /**
     * Repository used to manipulate subjects in database.
     */
    private final SubjectRepository subjectRepository;

    /**
     * Service used to make calls to external microservices.
     */
    private final OAuthCall oAuthCall;

    @Value("${forum.subjects.page-size}")
    private int pageSize;

    /**
     * Constructs <code>SubjectRepository</code> object.
     *
     * @param subjectRepository injected subject repository object.
     * @param oAuthCall injected <code>OAuthCall</code> service.
     */
    public SubjectServiceImpl(final SubjectRepository subjectRepository, final OAuthCall oAuthCall) {
        this.subjectRepository = subjectRepository;
        this.oAuthCall = oAuthCall;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Subject> getAll(final int categoryId, final int page) throws Exception {
        final Pageable pageable = new PageRequest(page, this.pageSize, Sort.Direction.ASC, "createDate");

        final Page<Subject> subjects = this.subjectRepository.findByCategoryId(categoryId, pageable);
        setUserInSubject(subjects);

        return subjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Subject> getAllSubject(final int page) throws Exception {
        final Pageable pageable = new PageRequest(page, this.pageSize, Sort.Direction.ASC, "lastDate");
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -2);
        final Page<Subject> subjects = this.subjectRepository.findAllByLastDateAfterOrderByLastDateDesc(calendar, pageable);
        setUserInSubject(subjects);

        return subjects;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subject getById(final int id) throws Exception {
        final Subject subject = this.subjectRepository.findOne(id);
        if (subject == null) {
            throw new DataRetrievalFailureException("Subject not found");
        }
        final Map<String, User> userMap = this.oAuthCall.getUsersByKeys(
                new HashSet<>(Arrays.asList(subject.getUserKey(), subject.getLastUserKey())));

        if (userMap.containsKey(subject.getUserKey())) {
            subject.setUser(userMap.get(subject.getUserKey()));
        }

        if (userMap.containsKey(subject.getLastUserKey())) {
            subject.setLastUser(userMap.get(subject.getLastUserKey()));
        }

        return subject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Subject save(final Subject subject) {
        return this.subjectRepository.save(subject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(final int id) {
        this.subjectRepository.delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCountView(final int id) {
        this.subjectRepository.updateCountView(id);
    }

    /**
     * Sets users in subject.
     *
     * @param subjects Subjects in which users need to be set.
     * @throws Exception thrown if can't get subjects or its authors.
     */
    private void setUserInSubject(Page<Subject> subjects) throws Exception {
        final HashSet<String> userKeys = new HashSet<>();
        subjects.forEach(subject -> {
            userKeys.add(subject.getUserKey());
            userKeys.add(subject.getLastUserKey());
        });

        final Map<String, User> userMap = this.oAuthCall.getUsersByKeys(userKeys);
        subjects.forEach(subject -> {
            if (userMap.containsKey(subject.getUserKey())) {
                subject.setUser(userMap.get(subject.getUserKey()));
            }

            if (userMap.containsKey(subject.getLastUserKey())) {
                subject.setLastUser(userMap.get(subject.getLastUserKey()));
            }
        });
    }
}