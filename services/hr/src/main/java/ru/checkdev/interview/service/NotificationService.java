package ru.checkdev.interview.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.checkdev.interview.domain.Interview;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class NotificationService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final String access;
    private final String urlNotify;
    private final String personUrl;
    private final String personAccess;
    private final TeamService teams;

    @Autowired
    public NotificationService(final @Value("${server.notification}") String urlNotify,
                               final @Value("${access.notification}") String access,
                               final @Value("${server.person}") String personUrl,
                               @Value("${access.person}") final String personAccess,
                               TeamService teams) {
        this.access = access;
        this.urlNotify = urlNotify;
        this.personUrl = personUrl;
        this.personAccess = personAccess;
        this.teams = teams;
    }

    public void send(Interview interview) {
        try {
            HashSet<String> users = new HashSet<>();
            users.add(interview.getVacancy().getKey());
            users.add(interview.getKey());
            Map<String, InterviewDetail.Person> persons = this.loadPersons(users);
            InterviewDetail.Person owner = persons.get(interview.getVacancy().getKey());
            List<TeamInfo> members = this.teams.findTeamsByOwner(owner.getKey());
            List<String> emails = new ArrayList<>();
            emails.add(owner.getEmail());
            members.stream().filter(
                    info -> info.getPermissions().values().stream().anyMatch(
                            vacancy -> interview.getVacancy().getId() == vacancy.getVacancy().getId()
                    )
            ).forEach(
                    info -> info.getPersons().values().forEach(
                            person -> emails.add(person.getEmail())
                    )
            );
            InterviewDetail.Person candidate = persons.get(interview.getKey());
            Map<String, Object> model = new HashMap<>();
            model.put("vacancy", interview.getVacancy());
            model.put("owner", owner);
            model.put("candidate", candidate);
            model.put("interview", interview);
            for (String email : emails) {
                new OAuthCall().doPost(
                        null,
                        String.format("%s/template/queue?access=%s", urlNotify, access),
                        new ObjectMapper().writeValueAsString(
                                new Notify(email, model, Notify.Type.RESULT_OF_INTERVIEW.name())
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, InterviewDetail.Person> loadPersons(HashSet<String> users) throws Exception {
        InterviewDetail.Person[] persons = this.mapper.readValue(
                new OAuthCall().doPost(
                        null,
                        String.format("%s/person/by?key=%s", this.personUrl, this.personAccess),
                        this.mapper.writeValueAsString(
                                users
                        )),
                InterviewDetail.Person[].class);
        return Stream.of(persons).collect(
                Collectors.toMap(
                        InterviewDetail.Person::getKey,
                        person -> person
                )
        );
    }

    /**
     * invite user to event.
     * @param notify notify
     * @return return
     */
    public void invite(Notify notify) {
        ObjectMapper object = new ObjectMapper();
        try {
            new OAuthCall().doPost(
                    null,
                    String.format("%s/template/queue?access=%s", urlNotify, access),
                    object.writeValueAsString(notify));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Calendar startOfDay(final Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Calendar endOfDay(Calendar calendar) {
        Calendar cal = (Calendar) calendar.clone();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal;
    }
}
