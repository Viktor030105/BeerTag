package com.example.beertag.repository;

import com.example.beertag.models.Beer;

import java.util.List;

public interface BeerRepository {

    List<Beer> getAllBeers();

    Beer getById(int id);

    Beer getByName(String name);

    void createBeer(Beer beer);

    void updateBeer(Beer beer);

    void deleteBeer(int id);
}
