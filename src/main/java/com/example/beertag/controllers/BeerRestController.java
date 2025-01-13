package com.example.beertag.controllers;


import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.helpers.BeerMapper;
import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.models.User;
import com.example.beertag.service.BeerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/beers")
public class BeerRestController {

    private final BeerService service;
    private final BeerMapper beerMapper;
    private final AuthenticationHelper helper;

    @Autowired
    public BeerRestController(BeerService service, BeerMapper beerMapper, AuthenticationHelper helper) {
        this.service = service;
        this.beerMapper = beerMapper;
        this.helper = helper;
    }

    @GetMapping
    public List<Beer> getAllBeers(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) Double minAbv,
                                  @RequestParam(required = false) Double maxAbv,
                                  @RequestParam(required = false) Integer styleId,
                                  @RequestParam(required = false) String sortBy,
                                  @RequestParam(required = false) String sortOrder) {
        return service.getAllBeers(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
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
    public Beer createBeer(@RequestHeader HttpHeaders headers, @Valid @RequestBody BeerDTO beerDTO) {
        try{
            User user = helper.tryGetUser(headers);

            Beer beer = beerMapper.fromDto(beerDTO);
            service.createBeer(beer);
            return beer;
        } catch (EntityNotFoundExeption ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (DublicateEntityExeption ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Beer updateBeer(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody BeerDTO beerDTO) {
        try{
            User user = helper.tryGetUser(headers);

            Beer beer = beerMapper.fromDto(id, beerDTO);
            service.updateBeer(beer, user);
            return beer;
        } catch (EntityNotFoundExeption ex){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (DublicateEntityExeption ex){
            throw new ResponseStatusException(HttpStatus.CONFLICT, ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBeer(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try{
            User user = helper.tryGetUser(headers);

            service.deleteBeer(id, user);
        } catch (EntityNotFoundExeption ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
