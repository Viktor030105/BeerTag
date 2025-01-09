package com.example.beertag.service;

import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import com.example.beertag.repository.BeerRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepositoryImpl repository;

    @Autowired
    public BeerServiceImpl(BeerRepositoryImpl repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> getAllBeers() {
        return repository.getAllBeers();
    }

    @Override
    public Beer getById(int id){
        return repository.getById(id);
    }

    @Override
    public void createBeer(Beer beer){
        boolean dublicateExists = true;

        try {
            repository.getByName(beer.getName());
        } catch (EntityNotFoundExeption ex){
            dublicateExists = false;
        }

        if (dublicateExists){
            throw new DublicateEntityExeption("Beer", "name", beer.getName());
        }

        repository.createBeer(beer);
    }

    @Override
    public void updateBeer(Beer beer){
        boolean dublicateExists = true;

        try {
            Beer existingBeer = repository.getByName(beer.getName());
            if (existingBeer.getId() == beer.getId()){
                dublicateExists = false;
            }
        } catch (EntityNotFoundExeption ex){
            dublicateExists = false;
        }

        if (dublicateExists){
            throw new DublicateEntityExeption("Beer", "name", beer.getName());
        }

        repository.updateBeer(beer);
    }

    @Override
    public void deleteBeer(int id){
        repository.deleteBeer(id);
    }
}
