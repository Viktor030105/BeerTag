package com.example.beertag.controllers;


import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import com.example.beertag.service.BeerServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private BeerServiceImpl service;

    public BeerRestController() {
        this.service = new BeerServiceImpl();
    }

    @GetMapping
    public List<Beer> getAllBeers() {
        return service.getAllBeers();
    }

    @GetMapping("/{id}")
    public Beer getBeerById(@PathVariable int id) {
        try{
            return service.getById(id);
        } catch (EntityNotFoundExeption ex){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage());
        }

    }

    @PostMapping
    public Beer createBeer(@RequestBody Beer beer) {
        try{
            service.createBeer(beer);
        } catch (DublicateEntityExeption ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }

        return beer;
    }

    @PutMapping("/{id}")
    public Beer updateBeer(@PathVariable int id, @RequestBody Beer beerToUpdate) {
        try{
            service.updateBeer(beerToUpdate);
        } catch (EntityNotFoundExeption ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (DublicateEntityExeption ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }

        return beerToUpdate;
    }

    @DeleteMapping("/{id}")
    public void deleteBeer(@PathVariable int id) {
        try{
            service.deleteBeer(id);
        } catch (EntityNotFoundExeption ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
