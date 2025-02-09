package com.example.beertag.controllers.mvc;

import com.example.beertag.exeptions.AuthenticationFailureException;
import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.helpers.ModelMapper;
import com.example.beertag.models.LoginDTO;
import com.example.beertag.models.RegisterDTO;
import com.example.beertag.models.User;
import com.example.beertag.service.UserService;
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

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public AuthenticationController(UserService userService, ModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDTO loginDTO
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

    @GetMapping("/logout")
    public String handleLogout(HttpSession session){
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDTO registerDTO
            , BindingResult bindingResult
            , HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        if (!registerDTO.getPassword().equals(registerDTO.getPasswordConfirm())){
            bindingResult.rejectValue("passwordConfirm",
                    "error.register.password",
                    "Password conformation must match password");
            return "register";
        }

        try {
            User user = modelMapper.fromDto(registerDTO);
            userService.createUser(user);
            return "redirect:/auth/login";
        } catch (DublicateEntityExeption e){
            bindingResult.rejectValue("username", "username.error", e.getMessage());
            return "register";
        }
    }
}
