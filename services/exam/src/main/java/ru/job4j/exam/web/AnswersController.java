package ru.job4j.exam.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.job4j.exam.domain.*;
import ru.job4j.exam.service.*;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RestController
@RequestMapping("/answers")
public class AnswersController {
    private final ExamService exams;

    private final QuestionService questions;

    private final AnswerService answers;

    private final QOptService options;

    private final ExamUserService users;

    @Autowired
    public AnswersController(final ExamService exams, final QuestionService questions,
                             AnswerService answers, QOptService options,
                             final ExamUserService users) {
        this.exams = exams;
        this.questions = questions;
        this.answers = answers;
        this.options = options;
        this.users = users;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/result/{examId}")
    public Object result(@PathVariable int examId, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        ExamUser euser = this.users.findByKeyAndExamId(key, examId);
        if (euser != null) {
            List<Hint> hints = this.users.getWrongAnswer(euser);
            return new Object() {
                public ExamUser getResult() {
                    return euser;
                }

                public List<Hint> getHints() {
                    return hints;
                }

                public boolean isWrong() {
                    return !hints.isEmpty();
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "illegal access";
                }
            };
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{examId}")
    public Object finish(@PathVariable int examId, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        ExamUser euser = this.users.findByKeyAndExamId(key, examId);
        if (euser != null) {
            if (this.isAvailable(euser)) {
                this.users.finish(euser);
                return new Object() {
                    public String getOk() {
                        return "ok";
                    }
                };
            } else {
                return new Object() {
                    public String getError() {
                        return "Вы можете пересдать экзамен через день.";
                    }
                };
            }
        } else {
            return new Object() {
                public String getError() {
                    return "illegal access";
                }
            };
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{examId}/{page}")
    public Object question(@PathVariable int examId, final @PathVariable int page, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        ExamUser euser = this.users.findByKeyAndExamId(key, examId);
        if (euser != null && this.isAvailable(euser)) {
            Long total = this.questions.countByExamId(examId);
            if (euser != null && total >= page) {
                final Question question = questions.findByExamId(examId, PageRequest.of(page, 1, Sort.Direction.ASC, "pos"))
                        .iterator().next();
                final Answer answer = this.answers.findByUserIdAndQuestionId(euser.getId(), question.getId());
                return new Object() {
                    public boolean isStart() {
                        return page <= 0;
                    }

                    public long getTotal() {
                        return total;
                    }

                    public List<AOpt> getAnswers() {
                        return answer != null ? answer.getOptions() : Collections.emptyList();
                    }

                    public boolean isFinish() {
                        return total == page + 1;
                    }

                    public Question getQuestion() {
                        return question;
                    }

                    public List<QOpt> getOptions() {
                        return options.findByQuestionId(question.getId());
                    }
                };
            } else {
                return new Object() {
                    public String getError() {
                        return "illegal access";
                    }
                };
            }
        } else {
            return new Object() {
                public String getError() {
                    return "Вы можете пересдать экзамен через день.";
                }
            };
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/options/{questionId}")
    public Object answers(@PathVariable int questionId, @RequestBody String[] options, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Question question = this.questions.findById(questionId);
        ExamUser euser = this.users.findByKeyAndExamId(key, question.getExam().getId());
        if (euser != null) {
            if (isAvailable(euser)) {
                return this.answers.save(euser, question, options);
            } else {
                return new Object() {
                    public String getError() {
                        return "Вы можете пересдать экзамен через день.";
                    }
                };
            }
        } else {
            return new Object() {
                public String getError() {
                    return "illegal access";
                }
            };
        }
    }

    @GetMapping("/profile")
    public List<ExamUser> start(@RequestParam String key) {
        return this.users.findByKey(key);
    }

    @GetMapping("/start/{id}")
    @PreAuthorize("isAuthenticated()")
    public Object start(@PathVariable int id, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        ExamUser exam = this.users.start(key, id);
        if (this.isAvailable(exam)) {
            this.users.cleanup(exam);
            return new Object() {
                public ExamUser getExamUser() {
                    return exam;
                }
            };
        } else {
            return new Object() {
                public String getError() {
                    return "Вы можете пересдать экзамен через день.";
                }
            };
        }

    }

    private boolean isAvailable(final ExamUser exam) {
        return exam.getFinish() == null
                || System.currentTimeMillis() - exam.getFinish().getTimeInMillis() > TimeUnit.DAYS.toMillis(1);
    }

    @GetMapping("/active")
    public Object getActiveExams() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("exams", exams.findByActive(true));
        map.put("average", exams.getAverage());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/active/{id}")
    public Object getByIdActive(@PathVariable int id) {
        Exam exam = this.exams.findByIdAndActive(id, true);
        return new Object() {
            public Exam getExam() {
                return exam;
            }

            public Details getDetails() throws Exception {
                return exams.getAverage(exam.getId());
            }
        };
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public List<ExamUser> getExamUser(Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return this.users.findByKey(key);
    }

    @GetMapping("/certificate")
    public List<ExamUser> certificate(@RequestParam String key) {
        return this.users.findByKey(key);
    }
}
