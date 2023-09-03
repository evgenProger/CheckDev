package ru.job4j.exam.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.exam.domain.Exam;
import ru.job4j.exam.domain.ExamUser;
import ru.job4j.exam.repository.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class ExamService {
    private final ExamRepository exams;
    private final ExamUserRepository users;
    private final QuestionRepository questions;
    private final QOptRepository qopts;
    private final AnswerRepository answers;
    private final AOptRepository aopts;

    private final String personUrl;
    private final String key;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public ExamService(final ExamRepository exams, final ExamUserRepository users,
                       QuestionRepository questions, QOptRepository qopts,
                       AnswerRepository answers, AOptRepository aopts,
                       @Value("${server.person}") final String personUrl, @Value("${access.person}") final String key) {
        this.exams = exams;
        this.users = users;
        this.questions = questions;
        this.qopts = qopts;
        this.answers = answers;
        this.aopts = aopts;
        this.personUrl = personUrl;
        this.key = key;
    }

    public Exam save(Exam exam) {
        return this.exams.save(exam);
    }

    public List<Exam> findAll() {
        return Lists.newArrayList(this.exams.findAll());
    }

    @Transactional
    public boolean delete(int id) {
        this.aopts.deleteByExamId(id);
        this.answers.deleteByExamId(id);
        this.users.deleteByExamId(id);
        this.qopts.deleteByExamId(id);
        this.questions.deleteByExamId(id);
        this.exams.deleteById(id);
        return true;
    }

    public Exam findById(int id) {
        return this.exams.findById(id).get();
    }

    public List<Exam> findByActive(boolean active) {
        return this.exams.findByActiveOrderByPosition(active);
    }

    public Map<Integer, Details> getAverage() throws Exception {
        Map<Integer, Details> result = new HashMap<>();
        for (Object[] data : this.users.getAverageInfo()) {
            Integer examId = (Integer) data[0];
            List<ExamUser> users = this.collect(this.users.getMax(examId).iterator(), 1);
            if (!users.isEmpty()) {
                result.put(
                        examId,
                        this.build((Long) data[1], (Long) data[2], users)
                );
            }
        }
        return result;
    }

    public Details getAverage(int examId) throws Exception {
        Object[] data = this.users.getAverageInfo(examId).iterator().next();
        List<ExamUser> users = this.collect(this.users.getMax(examId).iterator(), 5);
        if (!users.isEmpty()) {
            return this.build((Long) data[0], (Long) data[1], users);
        }
        return null;
    }

    public <T> List<T> collect(Iterator<T> it, int limit) {
        List<T> data = new ArrayList<>();
        int size = 0;
        while (it.hasNext() && size++ < limit) {
            data.add(it.next());
        }
        return data;
    }

    private Details build(Long total, Long average, List<ExamUser> users) throws Exception {
        ExamUser.Person[] persons =
                mapper.readValue(
                        new OAuthCall().doPost(
                                null,
                                String.format("%s/person/by?key=%s", this.personUrl, this.key),
                                mapper.writeValueAsString(
                                        users.stream().map(ExamUser::getKey).collect(Collectors.toList())
                                )),
                        ExamUser.Person[].class);
        List<Pair<ExamUser, ExamUser.Person>> best = new ArrayList<>();
        users.forEach(
                user -> {
                    best.add(Pair.of(user,
                            Stream.of(persons).filter(
                                    person -> person.getKey().equals(user.getKey())
                            ).findFirst().get())
                    );
                }
        );
        return new Details(total, average, best);
    }

    public Exam findByIdAndActive(int id, boolean active) {
        return this.exams.findByIdAndActive(id, active);
    }
}
