package com.example.beertag.service;

import com.example.beertag.exeptions.DublicateEntityExeption;
import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import com.example.beertag.repository.BeerRepositoryImpl;

import java.util.List;

public class BeerServiceImpl {

    private BeerRepositoryImpl repository;

    public BeerServiceImpl() {
        this.repository = new BeerRepositoryImpl();
    }

    public List<Beer> getAllBeers() {
        return repository.getAllBeers();
    }

    public Beer getById(int id){
        return repository.getById(id);
    }

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

    public void deleteBeer(int id){
        repository.deleteBeer(id);
    }
}
