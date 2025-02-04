package com.example.beertag.controllers.rest;

import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.helpers.ModelMapper;
import com.example.beertag.models.Beer;
import com.example.beertag.models.BeerDTO;
import com.example.beertag.models.FilterOptions;
import com.example.beertag.models.User;
import com.example.beertag.service.BeerService;
import jakarta.persistence.EntityNotFoundException;
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
    private final AuthenticationHelper authenticationHelper;
    private final ModelMapper modelMapper;

    @Autowired
    public BeerRestController(BeerService service, AuthenticationHelper authenticationHelper, ModelMapper modelMapper) {
        this.service = service;
        this.authenticationHelper = authenticationHelper;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<Beer> getAllBeers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Double minAbv,
            @RequestParam(required = false) Double maxAbv,
            @RequestParam(required = false) Integer styleId,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        FilterOptions filterOptions = new FilterOptions(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
        return service.getAll(filterOptions);
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
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Beer beer = modelMapper.fromDto(beerDTO);
            service.createBeer(beer, user);
            return beer;
        } catch (EntityNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (DublicateEntityExeption e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnknownError e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Beer updateBeer(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody BeerDTO beerDTO) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Beer beer = modelMapper.fromDto(beerDTO, id);
            service.updateBeer(beer, user);
            return beer;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityNotFoundExeption e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void deleteBeer(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            service.deleteBeer(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
}
