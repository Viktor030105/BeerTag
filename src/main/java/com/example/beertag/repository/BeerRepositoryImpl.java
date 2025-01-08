package com.example.beertag.repository;

import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class BeerRepositoryImpl {

    private List<Beer> beers;

    public BeerRepositoryImpl() {
        beers = new ArrayList<>();

        beers.add(new Beer(1, "Corona", 2.5));
        beers.add(new Beer(2, "Kamenica", 2.8));
        beers.add(new Beer(3, "Zagorka", 3.5));
    }

    public List<Beer> getAllBeers() {
        return beers;
    }

    public Beer getById(int id){
        return beers.stream()
                .filter(beer -> beer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Beer", id));
    }

    public Beer getByName(String name){
        return beers.stream()
                .filter(beer -> beer.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Beer", "name", name));
    }

    public void createBeer(Beer beer){
        beers.add(beer);
    }

    public void updateBeer(Beer beer){
        Beer beerToUpdate = getById(beer.getId());
        beerToUpdate.setName(beer.getName());
        beerToUpdate.setAbv(beer.getAbv());
    }

    public void deleteBeer(int id){
        Beer beerToDelete = getById(id);
        beers.remove(beerToDelete);
    }
}
