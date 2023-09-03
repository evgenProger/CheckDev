package ru.job4j.interview.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.interview.domain.*;
import ru.job4j.interview.repository.TAlgoRepository;
import ru.job4j.interview.service.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author parsentev
 * @since 26.09.2016
 */
@RequestMapping("/interview")
@RestController
public class InterviewController {

    private final TaskService tasks;

    private final InterviewService interviews;

    private final TAlgoRepository algos;

    private final ExamService exams;

    private final VacancyService vacancies;

    private final NotificationService notifications;

    @Autowired
    public InterviewController(final TaskService tasks, final InterviewService interviews,
                               final TAlgoRepository algos,
                               final ExamService exams, final VacancyService vacancies,
                               final NotificationService notifications) {
        this.tasks = tasks;
        this.interviews = interviews;
        this.algos = algos;
        this.exams = exams;
        this.vacancies = vacancies;
        this.notifications = notifications;
    }

    @GetMapping("/next/{interviewId}")
    public Object next(@PathVariable int interviewId, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        TrackState<Track, Track> tracks = interviews.nextTrack(interviewId);
        Track next = tracks.getNext();
        Map<String, Object> result = new HashMap<>();
        result.put("track", tracks.getNext());
        if (next != null) {
            result.put("all", interviews.countAllTaskByStageId(interviewId));
            result.put("passed", interviews.countPassedTaskByStageId(interviewId));
        }
        result.put("previous", tracks.getPrevious());
        result.put("last", tracks.last());
        if (next != null && next.getTask() != null
                && next.getTask().getType() == Task.Type.TASK) {
            result.put("algo",
                    algos.findById(
                            Integer.valueOf(
                                    next.getTask().getValues().iterator().next().getValue()
                            )
                    ).get()
            );
        }
        if (next != null && next.getTask() != null
                && next.getTask().getType() == Task.Type.TEST) {
            result.put("exam", exams.getExamResult(user, next.getTask().getValues().iterator().next().getValue()));
        }
        final Interview interview = interviews.findById(interviewId);
        final Vacancy vacancy = interview.getVacancy();
        if (vacancy.getLimit() != null) {
            result.put("limit", interview.getStart().getTimeInMillis() + (vacancy.getLimit() * 1000 * 60 * 60) - Calendar.getInstance().getTimeInMillis());
        }
        result.put("vacancy", vacancy);
        result.put("interview", interview);
        if (tracks.isNull()) {
            this.interviews.updateResult(interview.getId(), Interview.Result.SUCCESS, Calendar.getInstance());
            this.notifications.send(interview);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/upload/{trackId}")
    public Object handleFileUpload(@PathVariable int trackId, @RequestParam("file") MultipartFile file, Principal user) throws IOException {
        ITask iTask = new ITask();
        iTask.setValues(Collections.singletonList(
                new IValue(null, IValue.Key.SINGLE, file.getOriginalFilename(), file.getBytes())
        ));
        return interviews.save(trackId, iTask, user);
    }

    @PostMapping("/save/{trackId}")
    public Object save(@PathVariable int trackId, @RequestBody ITask task, Principal user) {
        return interviews.save(trackId, task, user);
    }

    @PostMapping("/start")
    public Interview start(@RequestBody Interview interview, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        interview.setKey(key);
        return interviews.start(interview);
    }

    @PostMapping("/sendInvite/{vacancyId}")
    public ResponseEntity<String> startInvite(@PathVariable int vacancyId, @RequestBody InterviewDetail.Person person) {
        Vacancy vacancy = vacancies.findById(vacancyId);
        Map<String, Object> keys = new HashMap<>();
        keys.put("vacancy", vacancy);
        Notify notify = new Notify(person.getEmail(), keys,
                Notify.Type.INVITE_TO_INTERVIEW.name());
        this.notifications.invite(notify);
        Interview interview = new Interview(Interview.Result.PROCESS, vacancy, person.getKey());
        interviews.start(interview);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @GetMapping("/interview/{vacancyId}")
    public Object interview(@PathVariable int vacancyId, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        final Interview interview = interviews.findByVacancyId(vacancyId, key);
        Map<String, Object> result = new HashMap<>();
        if (interview != null) {
            TrackState<Track, Track> tracks = interviews.nextTrack(interview.getId());
            result.put("process", true);
            if (!interview.getResult().equals(Interview.Result.OFFER)) {
                if (tracks.last() || tracks.noTracks()) {
                    result.put("last", true);
                }
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/candidate/{vacancyId}/{interviewId}")
    public Object candidate(@PathVariable int vacancyId, @PathVariable int interviewId, Principal user) {
        return new Object() {
            public VacancyDetails getVacancy() {
                return new VacancyDetails(
                        vacancies.findById(vacancyId),
                        interviews.getStatistic(vacancyId)
                );
            }

            public List<InterviewDetail> getCandidates() {
                return Collections.singletonList(interviews.findInterviewByVacancyId(
                        interviewId
                ));
            }

            public long getTotal() {
                return 1;
            }
        };
    }

    @GetMapping("/candidates/{vacancyId}/{result}/{search}/{limit}/{page}")
    public Object candidates(@PathVariable int vacancyId, @PathVariable String result, @PathVariable String search,
                             @PathVariable int limit, @PathVariable int page, Principal user) {
        final int pageToShow = page == 0 ? page : page - 1;
        Map<String, Object> data = new HashMap<>();
        data.put("vacancy",
                new VacancyDetails(
                        vacancies.findById(vacancyId),
                        interviews.getStatistic(vacancyId)
                )
        );
        data.put("candidates",
                interviews.findInterviewsByVacancyId(
                        vacancyId, result, search,
                        PageRequest.of(pageToShow, limit, Sort.Direction.ASC, "finish")
                )
        );
        data.put("total", interviews.countAllInterviewByVacancyId(vacancyId));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/details/{interviewId}")
    public Object details(@PathVariable int interviewId, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Map<String, Object> data = new HashMap<>();
        data.put("interview", this.interviews.findInterviewById(interviewId, key));
        List<ITask> tasks = this.interviews.findByTrack(interviewId);
        data.put("tracks", tasks);
        data.put("tasks", tasks.stream().collect(Collectors.toMap(ITask::getId, ITask::getTask)));
        return new ResponseEntity<>(data, HttpStatus.OK);
    }


    @GetMapping("/taken")
    public List<Interview> takenInterview(final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return this.interviews.taken(key);
    }

    @RequestMapping(path = "/download/{ivalueId}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable int ivalueId, Principal user) throws IOException {
        IValue value = this.interviews.findIValue(ivalueId);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(value.getAttach()));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename='" + value.getValue() + "'");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(value.getAttach().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @GetMapping("/comment/{interviewId}")
    public List<CommentInfo> comments(@PathVariable int interviewId, final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Interview interview = this.interviews.findInterviewById(interviewId, key);
        return this.interviews.findCommentsByInterviewId(interview.getId());
    }

    @PostMapping("/comment")
    public Comment addComment(@RequestBody Comment comment, final Principal user) throws IllegalAccessException {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        if (this.vacancies.hasAccess(comment.getInterview().getVacancy().getId(), key)) {
            comment.setOwner(key);
            comment.setCreated(Calendar.getInstance());
            return this.interviews.addComment(comment);
        }
        throw new IllegalAccessException();
    }

    @DeleteMapping("/")
    public boolean delete(@RequestParam int id, Principal user) throws IllegalAccessException {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Interview interview = this.interviews.findInterviewById(id);
        if (interview.getVacancy().getKey().equals(key)) {
            return this.interviews.deleteInterview(interview);
        } else {
            throw new IllegalAccessException("Don't have access");
        }
    }
}
