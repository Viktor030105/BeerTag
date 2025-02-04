package com.example.beertag.controllers.mvc;

import com.example.beertag.models.Beer;
import com.example.beertag.service.BeerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/beers")
public class BeerMvcController {

    private final BeerService beerService;

    @Autowired
    public BeerMvcController(BeerService beerService) {
        this.beerService = beerService;
    }

    @GetMapping("/{id}")
    public String showSingleBeer(@PathVariable int id, Model model) {
        try {
            Beer beer = beerService.getById(id);
            model.addAttribute("beer", beer);
            return "beer";
        } catch (EntityNotFoundException e) {
            return "not-found";
        }

    }
}
