package ru.checkdev.interview.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.interview.domain.*;
import ru.checkdev.interview.service.*;

import java.security.Principal;
import java.util.*;

/**
 * @author parsentev
 * @since 26.09.2016
 */
@RequestMapping("/vacancy")
@RestController
public class VacancyController {

    private final VacancyService vacancies;

    private final TaskService tasks;

    private final InterviewService interviews;

    private final NotificationService notification;

    @Autowired
    public VacancyController(final VacancyService vacancies,
                             final TaskService tasks, final InterviewService interviews, final NotificationService notification) {
        this.vacancies = vacancies;
        this.tasks = tasks;
        this.interviews = interviews;
        this.notification = notification;
    }

    @GetMapping("/ping")
    public String ping() {
        return "{}";
    }

    @GetMapping("/active/{limit}/{page}")
    public ResponseEntity<Map<String, Object>> getVacancies(@PathVariable int limit, @PathVariable int page) {
        final int pageToShow = page == 0 ? page : page - 1;
        Map<String, Object> res = new HashMap<>();
        res.put("vacancies", this.vacancies.findByActive(PageRequest.of(pageToShow, limit)));
        res.put("getTotal", vacancies.getCountOfVacancyActive());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/random")
    public ResponseEntity<Map<String, Object>> getRandomVacancies() {
        Map<String, Object> map = new HashMap<>();
        List<Vacancy> active = this.vacancies.findByActive(PageRequest.of(0, 5));
        map.put("vacancies", active);
        map.put("countVacancies", this.vacancies.getCountOfVacancyActive());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/search/{name}/{limit}/{page}")
    public ResponseEntity<Map<String, Object>> getVacanciesByName(@PathVariable String name, @PathVariable int limit, @PathVariable int page) {
        final int pageToShow = page == 0 ? page : page - 1;
        Map<String, Object> res = new HashMap<>();
        res.put("vacancies", this.vacancies.findByName(name, PageRequest.of(pageToShow, limit)));
        res.put("getTotal", this.vacancies.getCountByName(name));
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/info/{vacancyId}")
    public Vacancy info(@PathVariable int vacancyId) {
        return vacancies.findById(vacancyId);
    }

    @GetMapping("/{limit}/{page}")
    public Object getVacanciesByOwner(@PathVariable int limit, @PathVariable int page, Principal user) {
        final int pageToShow = page == 0 ? page : page - 1;
        Map<String, Object> map = new HashMap<>();
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        List<VacancyDetails> data = new ArrayList<>();
        for (Vacancy vacancy : vacancies.findByOwner(key, false,
                PageRequest.of(pageToShow, limit, Sort.Direction.ASC, "created"))) {
            data.add(new VacancyDetails(vacancy, interviews.getStatistic(vacancy.getId())));
        }
        map.put("vacancyDetails", data);
        map.put("total", vacancies.getCountOfVacancyBuPersonKey(key));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/invite/{limit}/{page}")
    public Object getVacanciesByOwnerForPerson(@PathVariable int limit, @PathVariable int page, @RequestParam String key, Principal user) {
        final int pageToShow = page == 0 ? page : page - 1;
        Map<String, Object> map = new HashMap<>();
        final String keyPrincipal = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        List<VacancyForCandidate> data = new ArrayList<>();
        for (Vacancy vacancy : vacancies.findByOwner(keyPrincipal, false,
                PageRequest.of(pageToShow, limit, Sort.Direction.ASC, "created"))) {

            data.add(new VacancyForCandidate(vacancy, interviews.find(vacancy.getId(), key)));
        }
        map.put("vacancyDetails", data);
        map.put("total", vacancies.getCountOfVacancyBuPersonKey(keyPrincipal));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @GetMapping("/all/{limit}/{page}")
    public Object getAllVacancy(@PathVariable int limit, @PathVariable int page, Principal user) {
        final int pageToShow = page == 0 ? page : page - 1;

        return new Object() {
            public List<VacancyDetails> getVacancyDetails() {
                List<VacancyDetails> data = new ArrayList<>();
                for (Vacancy vacancy : vacancies.findAll(
                        PageRequest.of(pageToShow, limit, Sort.Direction.ASC, "created"))) {
                    data.add(new VacancyDetails(vacancy, interviews.getStatisticAndUserData(vacancy.getId(), vacancy.getKey())));
                }
                return data;
            }

            public Long getTotal() {
                return vacancies.getCountOfAllVacancy();
            }
        };
    }

    @GetMapping("/archive")
    public List<Vacancy> getArchivedVacanciesByOwner(Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return new ArrayList<>(this.vacancies.findByOwner(key, true,
                PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "created")));
    }

    @GetMapping("/detail/{id}")
    public Object getVacancyById(@PathVariable int id) {
        return new Object() {
            public Vacancy getVacancy() {
                return vacancies.findById(id);
            }

            public List<Task> getTasks() {
                return tasks.findByVacancyId(id);
            }
        };
    }

    @GetMapping("/interview/{id}")
    public Object interview(@PathVariable int id) {
        return new Object() {
            public Vacancy getVacancy() {
                return vacancies.findById(id);
            }
        };
    }

    @PostMapping("/")
    public Vacancy save(@RequestBody Vacancy vacancy, Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        if (vacancy.getKey() == null) {
            vacancy.setKey(key);
        }
        return this.vacancies.save(vacancy);
    }

    @PostMapping("/invited/{id}")
    public Vacancy send(@PathVariable int id,
                        HttpEntity<String> httpEntity, Principal user) throws IllegalAccessException {
        Vacancy vacancy = this.vacancies.findById(id);
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        if (key.equals(vacancy.getKey())) {
            Map<String, Object> keys = new HashMap<>();
            keys.put("vacancy", vacancy);
            for (String email : httpEntity.getBody().split("\n")) {
                Notify notify = new Notify(email,
                        keys,
                        Notify.Type.INVITE_TO_INTERVIEW.name());
                this.notification.invite(notify);
            }
            return vacancy;
        } else {
            throw new IllegalAccessException("Don't have access");
        }
    }

    @GetMapping("/copy/{vacancyId}")
    public void copy(@PathVariable int vacancyId) {
        vacancies.copy(vacancyId);
    }

}
