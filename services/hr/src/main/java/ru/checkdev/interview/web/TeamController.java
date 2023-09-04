package ru.checkdev.interview.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.checkdev.interview.domain.Member;
import ru.checkdev.interview.domain.Permission;
import ru.checkdev.interview.domain.Team;
import ru.checkdev.interview.domain.Vacancy;
import ru.checkdev.interview.service.MemberInfo;
import ru.checkdev.interview.service.TeamInfo;
import ru.checkdev.interview.service.TeamService;
import ru.checkdev.interview.service.VacancyService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * @author Petr Arsentev (parsentev@yandex.ru)
 * @version $Id$
 * @since 0.1
 */
@RequestMapping("/team")
@RestController
public class TeamController {
    private final TeamService teams;

    private final VacancyService vacancies;

    @Autowired
    public TeamController(final TeamService teams, final VacancyService vacancies) {
        this.teams = teams;
        this.vacancies = vacancies;
    }

    @GetMapping("/owner")
    public Object teamByOwner(final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return new Object() {
            public List<TeamInfo> getTeams() {
                return teams.findTeamsByOwner(key);
            }

            public List<Vacancy> getVacancies() {
                return vacancies.findByOwner(key, false,
                        PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.ASC, "created"));
            }
        };
    }

    @GetMapping("/{teamId}")
    public Team teamByOwner(@PathVariable int teamId, final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return this.teams.findTeamByOwner(teamId, key);
    }

    @PostMapping("/")
    public Team create(@RequestBody Team team, final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        team.setOwner(key);
        return this.teams.create(team);
    }

    @PutMapping("/")
    public Team update(@RequestBody Team team, final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        team.setOwner(key);
        return this.teams.update(team);
    }

    @PostMapping("/member")
    public Member createMember(@RequestBody MemberInfo info, final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return this.teams.createMember(info);
    }

    @DeleteMapping("/member")
    public boolean deleteMember(@RequestParam int id, Principal user) throws IllegalAccessException {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Member member = this.teams.findMemberById(id);
        if (member.getTeam().getOwner().equals(key)) {
            return this.teams.deleteMember(member);
        } else {
            throw new IllegalAccessException("Don't have access");
        }
    }

    @PostMapping("/permission")
    public Permission createPermission(@RequestBody Permission info, final Principal user) {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        return this.teams.createPermission(info);
    }

    @DeleteMapping("/permission")
    public boolean deletePermission(@RequestParam int id, Principal user) throws IllegalAccessException {
        final String key = (String) ((Map) ((Map) ((OAuth2Authentication) user)
                .getUserAuthentication().getDetails()).get("principal")).get("key");
        Permission permission = this.teams.findPermissionById(id);
        if (permission.getTeam().getOwner().equals(key)) {
            return this.teams.deletePermission(permission);
        } else {
            throw new IllegalAccessException("Don't have access");
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "{}";
    }
}
