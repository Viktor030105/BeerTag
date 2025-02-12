package com.example.beertag.controllers.mvc;

import com.example.beertag.exeptions.AuthenticationFailureException;
import com.example.beertag.exeptions.DuplicateEntityException;
import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.helpers.ModelMapper;
import com.example.beertag.models.*;
import com.example.beertag.service.BeerService;
import com.example.beertag.service.StyleService;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/beers")
public class BeerMvcController {

    private final BeerService beerService;
    private final StyleService styleService;
    private final ModelMapper modelMapper;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public BeerMvcController(BeerService beerService, StyleService styleService, ModelMapper modelMapper, AuthenticationHelper authenticationHelper) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.modelMapper = modelMapper;
        this.authenticationHelper = authenticationHelper;
    }

    @ModelAttribute("styles")
    public List<Style> getStyles() {
        return styleService.getAll();
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
    public String showAllBeers(@ModelAttribute("filterDto") FilterOptionsDTO filterOptionsDTO, Model model) {
        FilterOptions filterOptions = new FilterOptions(
                filterOptionsDTO.getName(),
                filterOptionsDTO.getMinAbv(),
                filterOptionsDTO.getMaxAbv(),
                filterOptionsDTO.getStyleId(),
                filterOptionsDTO.getSortBy(),
                filterOptionsDTO.getSortOrder()
        );

        model.addAttribute("beers", beerService.getAll(filterOptions));
        return "beers";
    }

    @GetMapping("/{id}")
    public String showSingleBeer(@PathVariable int id, Model model) {
        try {
            Beer beer = beerService.getById(id);
            model.addAttribute("beer", beer);
            return "beer";
        } catch (Exception e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

    @GetMapping("/new")
    public String showNewBeerPage(Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        model.addAttribute("beer", new BeerDTO());
        return "beer-new";
    }

    @PostMapping("/new")
    public String createBeer(@Valid @ModelAttribute("beer") BeerDTO beer, BindingResult errors, HttpSession httpSession) {

        User user;
        try {
           user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "beer-new";
        }

        try {
            Beer newBeer = modelMapper.fromDto(beer, user);
            beerService.createBeer(newBeer, user);
            return "redirect:/beers";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "beer.exists", e.getMessage());
            return "beer-new";
        }
    }

    @GetMapping({"{id}/update"})
    public String showEditBeerPage(@PathVariable int id, Model model, HttpSession httpSession) {
        try {
            authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            Beer beer = beerService.getById(id);
            BeerDTO beerDTO = modelMapper.toDto(beer);
            model.addAttribute("beerId", id);
            model.addAttribute("beer", beerDTO);
            return "beer-update";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }

    }

    @PostMapping("/{id}/update")
    public String updateBeer(@PathVariable int id, @Valid @ModelAttribute("beer") BeerDTO beer,
                             BindingResult errors,
                             HttpSession httpSession, Model model) {

        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        if (errors.hasErrors()) {
            return "beer-update";
        }

        try {
            Beer newBeer = modelMapper.fromDto(beer, id);
            beerService.updateBeer(newBeer, user);

            return "redirect:/beers";
        } catch (DuplicateEntityException e) {
            errors.rejectValue("name", "beer.exists", e.getMessage());
            return "beer-update";
        } catch (EntityNotFoundException e){
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteBeer(@PathVariable int id, Model model, HttpSession httpSession) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(httpSession);
        } catch (AuthenticationFailureException e){
            return "redirect:/auth/login";
        }

        try {
            beerService.deleteBeer(id, user);

            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "not-found";
        } catch (UnauthorizedOperationException e){
            model.addAttribute("error", e.getMessage());
            return "access-denied";
        }
    }

}
