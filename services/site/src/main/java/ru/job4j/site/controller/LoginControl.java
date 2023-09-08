package ru.job4j.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.site.dto.CredentialDTO;
import ru.job4j.site.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@AllArgsConstructor
public class LoginControl {
    private final AuthService authService;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @PostMapping("/signIn")
    public String signIn(@ModelAttribute CredentialDTO credentialDTO,
                         HttpServletRequest req) throws JsonProcessingException {
        req.getSession()
                .setAttribute("token", authService.token(
                        Map.of("username", credentialDTO.getEmail(),
                                "password", credentialDTO.getPassword()))
                );
        return "redirect:/";
    }
}
