package ru.job4j.site.controller;

import org.springframework.ui.Model;
import ru.job4j.site.domain.Breadcrumb;
import ru.job4j.site.dto.UserInfoDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public class RequestResponseTools {

    public static String getToken(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("token");
    }

    public static void addAttrBreadcrumbs(Model model, String... args) {
        var list = new ArrayList<Breadcrumb>();
        for (int i = 0; i < args.length; i += 2) {
            list.add(new Breadcrumb(args[i], args[i + 1]));
        }
        model.addAttribute("breadcrumbs", list);
    }

    public static void addAttrCanManage(Model model, UserInfoDTO userInfo) {
        model.addAttribute("userInfo", userInfo);
        var canManage = userInfo.getRoles().stream()
                .anyMatch(role -> role.getValue().equals("ROLE_ADMIN"));
        model.addAttribute("canManage", canManage);
    }
}
