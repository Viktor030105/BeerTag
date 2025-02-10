package com.example.beertag.controllers.mvc;

import com.example.beertag.models.User;
import com.example.beertag.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMvcController {

    private final UserService userService;

    @Autowired
    public UserMvcController(UserService userService) {
        this.userService = userService;
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

//    @GetMapping({"{id}/update"})
//    public String showEditBeerPage(@PathVariable int id, Model model, HttpSession httpSession) {
//        try {
//            authenticationHelper.tryGetUser(httpSession);
//        } catch (AuthenticationFailureException e){
//            return "redirect:/auth/login";
//        }
//
//        try {
//            Beer beer = beerService.getById(id);
//            BeerDTO beerDTO = modelMapper.toDto(beer);
//            model.addAttribute("beerId", id);
//            model.addAttribute("beer", beerDTO);
//            return "beer-update";
//        } catch (EntityNotFoundException e){
//            model.addAttribute("error", e.getMessage());
//            return "not-found";
//        }
//
//    }
//
//    @PostMapping("/{id}/update")
//    public String updateBeer(@PathVariable int id, @Valid @ModelAttribute("beer") BeerDTO beer,
//                             BindingResult errors,
//                             HttpSession httpSession, Model model) {
//
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(httpSession);
//        } catch (AuthenticationFailureException e){
//            return "redirect:/auth/login";
//        }
//
//        if (errors.hasErrors()) {
//            return "beer-update";
//        }
//
//        try {
//            Beer newBeer = modelMapper.fromDto(beer, id);
//            beerService.updateBeer(newBeer, user);
//
//            return "redirect:/beers";
//        } catch (DublicateEntityExeption e) {
//            errors.rejectValue("name", "beer.exists", e.getMessage());
//            return "beer-update";
//        } catch (EntityNotFoundException e){
//            model.addAttribute("error", e.getMessage());
//            return "not-found";
//        } catch (UnauthorizedOperationException e){
//            model.addAttribute("error", e.getMessage());
//            return "access-denied";
//        }
//    }
//
//    @GetMapping("/{id}/delete")
//    public String deleteUser(@PathVariable int id, Model model, HttpSession httpSession) {
//        User user;
//        try {
//            user = authenticationHelper.tryGetUser(httpSession);
//        } catch (AuthenticationFailureException e){
//            return "redirect:/auth/login";
//        }
//
//        try {
//            userService.deleteUser(id);
//
//            return "redirect:/beers";
//        } catch (EntityNotFoundException e) {
//            model.addAttribute("error", e.getMessage());
//            return "not-found";
//        } catch (UnauthorizedOperationException e){
//            model.addAttribute("error", e.getMessage());
//            return "access-denied";
//        }
//    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }
}
