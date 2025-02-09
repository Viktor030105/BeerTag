package com.example.beertag.controllers.mvc;

import com.example.beertag.exeptions.AuthenticationFailureException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.models.LoginDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationController(AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String HandleLogin(@Valid @ModelAttribute("login") LoginDTO loginDTO
            , BindingResult bindingResult
            , HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            authenticationHelper.verifyAuthentication(loginDTO.getUsername(), loginDTO.getPassword());
            session.setAttribute("currentUser", loginDTO.getUsername());

            return "redirect:/";

        } catch (AuthenticationFailureException e) {
            bindingResult.rejectValue("username", "error.login", e.getMessage());

            return "login";
        }
    }
}
