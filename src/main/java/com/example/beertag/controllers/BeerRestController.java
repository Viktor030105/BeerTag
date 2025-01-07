package com.example.beertag.controllers;


import com.example.beertag.models.Beer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private final List<Beer> beers;

    public BeerRestController() {
        beers = new ArrayList<>();
        beers.add(new Beer(1, "Corona", 2.5));
        beers.add(new Beer(2, "Kamenica", 2.8));
        beers.add(new Beer(3, "Zagorka", 3.5));
    }

    @GetMapping
    public List<Beer> getBeers() {
        return beers;
    }

    @PostMapping
    public Beer createBeer(@RequestBody Beer beer) {
        if (beer.getName() == null || beer.getName().isEmpty() || beer.getAbv() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid beer data");
        }
        beers.add(beer);
        return beer;
    }

    @PutMapping("/{id}")
    public Beer updateBeer(@PathVariable int id, @RequestBody Beer beerToUpdate) {
        Beer existingBeer = beers.stream()
                .filter(beer -> beer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Beer with id %d not found", id)
                ));
        existingBeer.setName(beerToUpdate.getName());
        existingBeer.setAbv(beerToUpdate.getAbv());
        return existingBeer;
    }

    @DeleteMapping("/{id}")
    public void deleteBeer(@PathVariable int id) {
        beers.removeIf(beer -> beer.getId() == id);
    }
}
