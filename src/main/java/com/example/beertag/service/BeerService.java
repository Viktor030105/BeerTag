package com.example.beertag.service;

import com.example.beertag.models.Beer;

import java.util.List;

public interface BeerService {

    List<Beer> getAllBeers();

    Beer getById(int id);

    void createBeer(Beer beer);

    void updateBeer(Beer beer);

    void deleteBeer(int id);
}
