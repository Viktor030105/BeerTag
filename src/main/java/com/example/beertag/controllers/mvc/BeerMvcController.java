package com.example.beertag.controllers.mvc;

import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.helpers.ModelMapper;
import com.example.beertag.models.*;
import com.example.beertag.service.BeerService;
import com.example.beertag.service.StyleService;
import com.example.beertag.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
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
    private final UserService userService;

    @Autowired
    public BeerMvcController(BeerService beerService, StyleService styleService, ModelMapper modelMapper, UserService userService) {
        this.beerService = beerService;
        this.styleService = styleService;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @ModelAttribute("styles")
    public List<Style> getStyles() {
        return styleService.getAll();
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String showAllBeers(Model model) {
        List<Beer> beers = beerService.getAll(new FilterOptions(null, null, null, null, null, null));
        model.addAttribute("beers", beers);
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
    public String showNewBeerPage(Model model) {
        model.addAttribute("beer", new BeerDTO());
        return "beer-new";
    }

    @PostMapping("/new")
    public String createBeer(@Valid @ModelAttribute("beer") BeerDTO beer, BindingResult errors) {

        if (errors.hasErrors()) {
            return "beer-new";
        }

        try {
            User creator = userService.getById(1);
            Beer newBeer = modelMapper.fromDto(beer, creator);
            beerService.createBeer(newBeer, creator);
            return "redirect:/beers";
        } catch (DublicateEntityExeption e) {
            errors.rejectValue("name", "beer.exists", e.getMessage());
            return "beer-new";
        }
    }

    @GetMapping({"{id}/update"})
    public String showEditBeerPage(@PathVariable int id, Model model) {
        Beer beer = beerService.getById(id);
        BeerDTO beerDTO = modelMapper.toDto(beer);
        model.addAttribute("beer", beerDTO);
        return "beer-update";
    }

    @PostMapping("/{id}/update")
    public String updateBeer(@PathVariable int id, @Valid @ModelAttribute("beer") BeerDTO beer, BindingResult errors) {

        if (errors.hasErrors()) {
            return "beer-update";
        }

        try {
            User user = userService.getById(1);
            Beer newBeer = modelMapper.fromDto(beer, id);
            beerService.updateBeer(newBeer, user);
            return "redirect:/beers";
        } catch (DublicateEntityExeption e) {
            errors.rejectValue("name", "beer.exists", e.getMessage());
            return "beer-update";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteBeer(@PathVariable int id, Model model) {
        try {
            User user = userService.getById(1);
            beerService.deleteBeer(id, user);
            return "redirect:/beers";
        } catch (Exception e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "not-found";
        }
    }

}
