package ru.job4j.site.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.site.dto.UserInfoDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.ProfilesService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

/**
 * CheckDev пробное собеседование
 * ProfilesController класс обработки запросов профилей.
 *
 * @author Dmitry Stepanov
 * @version 23.04.2023T10:31
 */
@Controller
@RequestMapping("/profiles")
@Slf4j
public class ProfilesController {
    private final String key;
    private final ProfilesService profilesService;
    private final AuthService authService;

    public ProfilesController(@Value("${server.auth.access.key}") String key,
                              ProfilesService profilesService, AuthService authService) {
        this.key = key;
        this.profilesService = profilesService;
        this.authService = authService;
    }

    /**
     * Отображение вида одного ProfileDTO
     *
     * @param id    ID Profile
     * @param model Model
     * @return String "oneProfile" page
     */
    @GetMapping("/{id}")
    public String getProfileById(@PathVariable int id, Model model, HttpServletRequest request) {
        var profileOptional = profilesService.getProfileById(id, key);
        profileOptional.ifPresent(
                p -> model.addAttribute("profile", p)
        );
        model.addAttribute("userInfo", getUserInfo(request));
        return "/oneProfile";
    }

    /**
     * Отображение вида всех профилей List<ProfileDTO>
     *
     * @param model Model
     * @return String "/profiles" pge
     */
    @GetMapping("/")
    public String getAllProfiles(Model model, HttpServletRequest request) {
        var profilesList = profilesService.getAllProfile(key);
        model.addAttribute("profiles", profilesList);
        model.addAttribute("userInfo", getUserInfo(request));
        return "/profiles";
    }

    private UserInfoDTO getUserInfo(HttpServletRequest request) {
        var token = getToken(request);
        if (token == null) {
            return null;
        }
        try {
            return authService.userInfo(token);
        } catch (Exception e) {
            log.error("UserInfo data available. {}", e.getMessage());
            return null;
        }
    }
}
