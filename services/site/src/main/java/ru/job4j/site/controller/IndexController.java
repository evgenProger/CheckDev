package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.site.dto.UserInfoDTO;
import ru.job4j.site.service.AuthService;
import ru.job4j.site.service.CategoriesService;

import javax.servlet.http.HttpServletRequest;

import static ru.job4j.site.controller.RequestResponseTools.getToken;

@Controller
@AllArgsConstructor
@Slf4j
public class IndexController {
    private final AuthService authService;
    private final CategoriesService categoriesService;

    @GetMapping({"/", "index"})
    public String getIndexPage(@RequestParam(value = "error", required = false) String error,
                               Model model, HttpServletRequest request) throws JsonProcessingException {
        RequestResponseTools.addAttrBreadcrumbs(model,
                "Главная", "/index",
                "Категории", "/categories/"
        );
        model.addAttribute("categories", categoriesService.getAll());
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Email or Password is incorrect !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("userInfo", getUserInfo(request));
        return "index";
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
