package com.example.beertag.controllers.mvc;

import com.example.beertag.exeptions.AuthenticationFailureException;
import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundException;
import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.helpers.ModelMapper;
import com.example.beertag.models.User;
import com.example.beertag.models.UserDTO;
import com.example.beertag.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @Autowired
    public UserMvcController(UserService userService, AuthenticationHelper authenticationHelper, ModelMapper modelMapper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model) {
        try {
            User user = userService.getById(id);
            model.addAttribute("user", user);
            return "user";
        } catch (Exception e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping({"{id}/update"})
    public String showEditUserPage(@PathVariable int id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            User user = userService.getById(id);
            UserDTO userDTO = modelMapper.toDto(user);
            model.addAttribute("userId", id);
            model.addAttribute("user", userDTO);
            return "user-update";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }

    }
//TODO

    @PostMapping("/{id}/update")
    public String updateUser(@PathVariable int id, @Valid @ModelAttribute("user") UserDTO user,
                             BindingResult errors,
                             HttpSession httpSession, Model model) {

        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "user-update";
        }

        try {
            User newUser = modelMapper.fromDto(user, id);
            userService.updateUser(newUser);

            return "redirect:/users";
        } catch (DublicateEntityExeption e) {
            errors.rejectValue("name", "user.exists", e.getMessage());
            return "user-update";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable int id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            userService.deleteBeer(id);

            return "redirect:/beers";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }
}
