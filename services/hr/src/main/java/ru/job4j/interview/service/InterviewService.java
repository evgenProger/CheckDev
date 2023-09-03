package ru.job4j.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.interview.domain.*;
import ru.job4j.interview.repository.*;

import javax.annotation.PreDestroy;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.job4j.interview.domain.Track.Key.FAILURE;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class InterviewService {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final InterviewRepository interviews;
    private final ITaskRepository itasks;
    private final TaskRepository tasks;
    private final TrackRepository trackRepository;
    private final IValueRepository ivalues;
    private final String personUrl;
    private final ExamService exams;
    private final String personAccess;
    private final ValidateResult validated;
    private final CommentRepository comments;
    private final NotificationService notifications;
    private final VacancyRepository vacancy;

    @Autowired
    public InterviewService(final @Value("${server.person}") String personUrl,
                            InterviewRepository interviews, ITaskRepository itasks,
                            TaskRepository tasks, TrackRepository tracks, IValueRepository ivalues,
                            final ExamService exams, final VacancyRepository vacancy,
                            @Value("${access.person}") final String personAccess, ValidateResult validated,
                            final CommentRepository comments, final NotificationService notifications) {
        this.interviews = interviews;
        this.itasks = itasks;
        this.tasks = tasks;
        this.trackRepository = tracks;
        this.ivalues = ivalues;
        this.personUrl = personUrl;
        this.exams = exams;
        this.personAccess = personAccess;
        this.validated = validated;
        this.comments = comments;
        this.notifications = notifications;
        this.vacancy = vacancy;
    }

    @Transactional
    public Track save(int trackId, ITask itask, Principal user) {
        Track track = this.trackRepository.findById(trackId).get();
        Task task = track.getTask();
        Track.Key result = Track.Key.PASSED;
        if (task != null) {
            itask.setTask(task);
            itask.setInterview(track.getInterview());
            itask.setStart(Calendar.getInstance());
            itask.setFinish(Calendar.getInstance());
            List<IValue> values = itask.getValues();
            if (task.getType() == Task.Type.TEST) {
                ExamService.ExamInfo exam = exams.getExamResult(user, task.getValues().iterator().next().getValue());
                IValue value = new IValue();
                value.setKey(IValue.Key.SINGLE);
                value.setTask(itask);
                value.setValue(String.valueOf(exam.getResult()));
                values.set(0, value);
            }
            itask = this.itasks.save(itask);
            for (IValue value : values) {
                value.setTask(itask);
                this.ivalues.save(value);
            }
            result = this.validated.by(itask, task);
        } else if (track.getKey() == Track.Key.PASSED) {
            result = Track.Key.SUCCESS;
        }
        this.trackRepository.update(trackId, true, result);
        if (this.isLast(track.getInterview().getId(), trackId)) {
            if (this.calculateResult(track.getInterview()) != Interview.Result.FAIL) {
                this.notifications.send(track.getInterview());
            }
        }
        return track;
    }

    private Interview.Result calculateResult(Interview interview) {
        List<Track> tracks = this.trackRepository.findByInterviewIdOrderByPositionAsc(interview.getId());
        Interview.Result rsl = Interview.Result.SUCCESS;
        for (Track track : tracks) {
            if (track.getKey() == FAILURE) {
                rsl = Interview.Result.FAIL;
                break;
            }
        }
        interview = this.interviews.findById(interview.getId()).get();
        final Integer limit = this.vacancy.findById(interview.getVacancy().getId()).get().getLimit();
        if ((limit != null) && (Calendar.getInstance().getTimeInMillis() - interview.getStart().getTimeInMillis()) >= limit * 1000 * 60 * 60) {
            rsl = Interview.Result.FAIL;
        }
        this.interviews.updateRsl(interview.getId(), rsl, Calendar.getInstance());
        return rsl;
    }

    public void updateResult(int id, Interview.Result rsl, Calendar finish) {
        this.interviews.updateRsl(id, rsl, finish);
    }

    public boolean isLast(int interviewId, int trackId) {
        List<Track> tracks = this.trackRepository.findByInterviewIdOrderByPositionAsc(interviewId);
        return tracks.get(tracks.size() - 1).getId() == trackId;
    }

    public TrackState<Track, Track> nextTrack(int interviewId) {
        List<Track> tracks = this.trackRepository.findByInterviewIdOrderByPositionAsc(interviewId);
        List<Track> passed = tracks.stream().filter(Track::isPassed).collect(Collectors.toList());
        Track current = passed.isEmpty() ? null : passed.get(passed.size() - 1);
        Track next = tracks.stream().filter(t -> !t.isPassed()).findFirst().orElse(null);
        return new TrackState<>(current, next);
    }

    public Long countPassedTaskByStageId(int interviewId) {
        return this.trackRepository.countPassedTaskByInterviewId(true, interviewId);
    }

    public Long countAllTaskByStageId(int interviewId) {
        return this.trackRepository.countAllTaskByInterviewId(interviewId);
    }

    @Transactional
    public Interview start(Interview interview) {
        int vacancyId = interview.getVacancy().getId();
        Interview result = this.interviews.findByVacancyIdAndKey(vacancyId, interview.getKey());
        if (result == null) {
            interview.setStart(Calendar.getInstance());
            interview = interviews.save(interview);
            List<Track> tracks = new ArrayList<Track>(100);
            int position = 0;
            for (Task task : this.tasks.findByVacancyIdByPosAsc(vacancyId)) {
                tracks.add(new Track(interview, task, false, position++, Calendar.getInstance()));
            }
            tracks.forEach(trackRepository::save);
        }
        return result == null ? interview : result;
    }

    public Interview findByKey(int vacancyId, String key) {
        return this.interviews.findByVacancyIdAndKey(vacancyId, key);
    }

    public Interview save(Interview interview) {
        return this.interviews.save(interview);
    }


    public Interview findInterviewById(int interviewId) {
        return this.interviews.findById(interviewId).get();
    }

    public Interview findByVacancyId(int vacancyId, String key) {
        return this.interviews.findByVacancyIdAndKey(vacancyId, key);
    }

    /**
     * delete from track;
     * delete from comment;
     * delete from interview_value;
     * delete from interview_task;
     * delete from interview;
     *
     * @param interview Interview
     * @return
     */
    @Transactional
    public boolean deleteInterview(Interview interview) {
        this.trackRepository.deleteByInterviewId(interview.getId());
        this.comments.deleteByInterviewId(interview.getId());
        this.ivalues.deleteByInterviewId(interview.getId());
        this.itasks.deleteByInterviewId(interview.getId());
        this.interviews.deleteByInterviewId(interview.getId());
        return true;
    }

    /**
     * TODO check permission by team member.
     *
     * @param interviewId interview id.
     * @param key         owner.
     * @return interview.
     */
    public Interview findInterviewById(int interviewId, String key) {
        return this.interviews.findById(interviewId).get();
    }

    public List<InterviewDetail> findInterviewsByVacancyId(int vacancyId, String result, String search, Pageable pageable) {
        List<Interview> interviews;
        if ("total".equalsIgnoreCase(result)) {
            interviews = this.interviews.findByVacancyId(vacancyId, pageable);
        } else {
            interviews = this.interviews.findByVacancyIdAndResult(vacancyId,
                    Interview.Result.valueOf(result.toUpperCase()), pageable);
        }
        Map<Integer, String> keys = interviews.stream().collect(Collectors.toMap(
                Interview::getId,
                Interview::getKey
        ));
        Map<String, Interview> interviewMap = interviews.stream().collect(Collectors.toMap(
                Interview::getKey,
                e -> e
        ));
        return getInterviewDetails(interviews, keys, interviewMap, search);
    }

    private List<InterviewDetail> getInterviewDetails(List<Interview> interviews, Map<Integer, String> keys,
                                                      Map<String, Interview> interviewMap, String search) {
        List<InterviewDetail> details = new ArrayList<>(interviews.size());
        for (InterviewDetail.Person person : new OAuthCall().collectPerson(keys.values(), this.personUrl, this.personAccess)) {
            Interview interview = interviewMap.get(person.getKey());
            if (person.getUsername().toLowerCase().contains(search.toLowerCase())
                    | person.getEmail().toLowerCase().contains(search.toLowerCase())
                    | search.equals(" ")) {
                details.add(
                        new InterviewDetail(
                                interview,
                                person,
                                Collections.EMPTY_LIST,
                                Collections.EMPTY_MAP
                        )
                );
            }
        }
        details.sort((o1, o2) -> -o1.getInterview().getStart().compareTo(o2.getInterview().getStart()));
        return details;
    }

    private <K, V> Map<K, List<V>> convert(List<V> list, Function<V, K> key) {
        Map<K, List<V>> tracks = new HashMap<>();
        list.forEach(
                t -> {
                    K keyId = key.apply(t);
                    if (!tracks.containsKey(keyId)) {
                        tracks.put(keyId, new ArrayList<>());
                    }
                    tracks.get(keyId).add(t);
                }
        );
        return tracks;
    }


    public long countAllInterviewByVacancyId(int vacancyId) {
        return this.interviews.countAllInterviewByVacancyId(vacancyId);
    }

    @PreDestroy
    public void close() {
        this.scheduler.shutdown();
    }

    public VacancyDetails.Statistic getStatistic(int vacancyId) {
        return new VacancyDetails.Statistic(
                this.interviews.countAllInterviewByVacancyId(vacancyId),
                this.interviews.getStatistic(vacancyId, Interview.Result.SUCCESS),
                this.interviews.getStatistic(vacancyId, Interview.Result.FAIL),
                this.interviews.getStatistic(vacancyId, Interview.Result.REJECT),
                this.interviews.getStatistic(vacancyId, Interview.Result.HIRE)
        );
    }

    public VacancyDetails.Statistic getStatisticAndUserData(int vacancyId, String key) {
        return new VacancyDetails.Statistic(
                this.interviews.countAllInterviewByVacancyId(vacancyId),
                this.interviews.getStatistic(vacancyId, Interview.Result.SUCCESS),
                this.interviews.getStatistic(vacancyId, Interview.Result.FAIL),
                this.interviews.getStatistic(vacancyId, Interview.Result.REJECT),
                this.interviews.getStatistic(vacancyId, Interview.Result.HIRE),
                new OAuthCall().collectPerson(
                        Collections.singletonList(key),
                        this.personUrl, this.personAccess)[0]

        );
    }

    /**
     * Return taken interview by key.
     *
     * @param key user key
     * @return list of taken interview.
     */
    public List<Interview> taken(final String key) {
        return this.interviews.findByKey(key);
    }

    public List<Interview> findByFinishBetween(Calendar start, Calendar finish) {
        return this.interviews.findByFinishBetween(start, finish);
    }

    /**
     * Save a new comment.
     *
     * @param comment comment.
     * @return persist comment.
     */
    public Comment addComment(Comment comment) {
        return this.comments.save(comment);
    }

    public List<CommentInfo> findCommentsByInterviewId(int interviewId) {
        List<CommentInfo> data = new ArrayList<>();
        this.comments.findByInterviewIdOrderByCreated(interviewId).forEach(
                comment -> data.add(new CommentInfo(
                                comment,
                                new OAuthCall().collectPerson(
                                        Collections.singletonList(comment.getOwner()),
                                        this.personUrl, this.personAccess)[0]
                        )
                )
        );
        return data;
    }

    public InterviewDetail findInterviewByVacancyId(int interviewId) {
        Interview interview = this.interviews.findById(interviewId).get();
        return new InterviewDetail(
                interview,
                new OAuthCall().collectPerson(
                        Collections.singletonList(interview.getKey()),
                        this.personUrl, this.personAccess)[0],
                this.trackRepository.findByInterviewIdOrderByPositionAsc(interviewId),
                this.itasks.findByInterviewId(interviewId).stream().collect(
                        Collectors.toMap(
                                t -> t.getTask().getId(),
                                t -> t
                        )
                )
        );
    }

    public IValue findIValue(int ivalueId) {
        return this.ivalues.findById(ivalueId).get();
    }

    public Interview findById(int id) {
        return this.interviews.findById(id).get();
    }

    public Interview.Result find(int vacancyId, String key) {
        Interview.Result result = null;
        Interview vacancyIdAndKey = this.interviews.findByVacancyIdAndKey(vacancyId, key);
        if (vacancyIdAndKey != null) {
            result = vacancyIdAndKey.getResult();
        }
        return result;
    }

    public List<ITask> findByTrack(int interviewId) {
        return this.itasks.findByInterviewId(interviewId);
    }
}
