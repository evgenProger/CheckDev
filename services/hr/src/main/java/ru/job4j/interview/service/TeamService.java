package ru.job4j.interview.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.job4j.interview.domain.Member;
import ru.job4j.interview.domain.Permission;
import ru.job4j.interview.domain.Team;
import ru.job4j.interview.repository.MemberRepository;
import ru.job4j.interview.repository.PermissionRepository;
import ru.job4j.interview.repository.TeamRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**

 *
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@Service
public class TeamService {
    private final TeamRepository teams;

    private final MemberRepository members;

    private final PermissionRepository permissions;
    private final String url;
    private final String access;

    @Autowired
    public TeamService(final TeamRepository teams, final MemberRepository members,
                       final PermissionRepository permissions,
                       final @Value("${server.person}") String personUrl,
                       @Value("${access.person}") final String personAccess) {
        this.teams = teams;
        this.members = members;
        this.permissions = permissions;
        this.url = personUrl;
        this.access = personAccess;
    }

    public List<TeamInfo> findTeamsByOwner(String key) {
        List<TeamInfo> members = new ArrayList<>();
        List<Team> teams = this.teams.findByOwner(key);
        List<String> personKeys = new ArrayList<>();
        teams.forEach(
                t -> {
                    t.getMembers().forEach(
                            member -> personKeys.add(member.getPerson())
                    );
                }
        );
        InterviewDetail.Person[] keys = new OAuthCall().collectPerson(personKeys, this.url, this.access);
        Map<String, InterviewDetail.Person> persons = Stream.of(keys).collect(
                Collectors.toMap(
                        InterviewDetail.Person::getKey,
                        person -> person
                )
        );
        List<Permission> vacancies = this.permissions.findByTeamIdIn(
                teams.stream().map(Team::getId).collect(Collectors.toList())
        );
        teams.forEach(
                team -> members.add(
                        new TeamInfo(
                                team,
                                team.getMembers().stream()
                                        .collect(
                                                Collectors.toMap(
                                                        Member::getId,
                                                        m -> persons.get(m.getPerson())
                                                )
                                        ),
                                vacancies.stream().filter(
                                        permission -> permission.getTeam().getId() == team.getId()
                                ).collect(
                                        Collectors.toMap(
                                                Permission::getId,
                                                p -> new VacancyInfo(p, p.getVacancy()))
                                )
                        )
                )
        );
        return members;
    }

    public Team create(Team team) {
        return this.teams.save(team);
    }

    public Team update(Team team) {
        if (this.teams.findById(team.getId()).get().getOwner().equals(team.getOwner())) {
            return this.teams.save(team);
        }
        throw new IllegalStateException();
    }

    public Team findTeamByOwner(int teamId, String key) {
        return this.teams.findByIdAndOwner(teamId, key);
    }

    public Member createMember(MemberInfo info) {
        InterviewDetail.Person person = new OAuthCall().findByEmail(info.getEmail(), url, access);
        if (!person.getEmail().equals(info.getEmail())) {
            throw new UnsupportedOperationException();
        }
        Member member = new Member();
        member.setPerson(person.getKey());
        member.setTeam(info.getTeam());
        return this.members.save(member);
    }

    /**
     * Find member by id.
     * @param id member id.
     * @return model or null.
     */
    public Member findMemberById(int id) {
        return this.members.findById(id).get();
    }

    /**
     * Delete members.
     * @param member member.
     * @return true if all is ok.
     */
    public boolean deleteMember(Member member) {
        this.members.delete(member);
        return true;
    }

    public Permission createPermission(Permission info) {
        return this.permissions.save(info);
    }

    public boolean deletePermission(Permission info) {
        this.permissions.delete(info);
        return true;
    }

    public Permission findPermissionById(int id) {
        return this.permissions.findById(id).get();
    }
}
