package ru.checkdev.interview.service;

import ru.checkdev.interview.domain.Team;

import java.util.Map;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
public class TeamInfo {
    private final Team team;

    private final Map<Integer, InterviewDetail.Person> persons;

    private final Map<Integer, VacancyInfo> permissions;

    public TeamInfo(Team team, Map<Integer, InterviewDetail.Person> persons,
                    Map<Integer, VacancyInfo> permissions) {
        this.team = team;
        this.persons = persons;
        this.permissions = permissions;
    }

    public Team getTeam() {
        return team;
    }

    public Map<Integer, InterviewDetail.Person> getPersons() {
        return persons;
    }

    public Map<Integer, VacancyInfo> getPermissions() {
        return permissions;
    }
}
