package com.example.beertag.service;

import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundException;
import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.models.Beer;
import com.example.beertag.models.FilterOptions;
import com.example.beertag.models.User;
import com.example.beertag.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BeerServiceImpl implements BeerService {

    private static final String MODIFY_BEER_ERROR_MESSAGE = "Only admin or beer creator can modify a beer.";

    private final BeerRepository repository;

    @Autowired
    public BeerServiceImpl(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> getAll(FilterOptions filterOptions) {
        return repository.getAll(filterOptions);
    }

    @Override
    public Beer getById(int id){
        return repository.getById(id);
    }

    @Override
    public void createBeer(Beer beer, User user) {
        boolean duplicateExists = true;
        try {
            repository.getByName(beer.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DublicateEntityExeption("Beer", "name", beer.getName());
        }

        beer.setCreatedBy(user);
        repository.createBeer(beer);
    }

    @Override
    public void updateBeer(Beer beer, User user){
        checkModifyPermissions(beer.getId(), user);

        boolean duplicateExists = true;
        try {
            Beer existingBeer = repository.getByName(beer.getName());
            if (existingBeer.getId() == beer.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DublicateEntityExeption("Beer", "name", beer.getName());
        }

        repository.updateBeer(beer);
    }

    @Override
    public void deleteBeer(int id, User user){
        checkModifyPermissions(id, user);
        repository.deleteBeer(id);
    }

    private void checkModifyPermissions(int beerId, User user) {
        Beer beer = repository.getById(beerId);
        if (!(user.isAdmin() || beer.getCreatedBy().equals(user))) {
            throw new UnauthorizedOperationException(MODIFY_BEER_ERROR_MESSAGE);
        }
    }
}
