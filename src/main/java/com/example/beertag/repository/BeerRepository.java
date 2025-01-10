package com.example.beertag.repository;

import com.example.beertag.models.Beer;

import java.util.List;

public interface BeerRepository {

    List<Beer> getAllBeers(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder);

    Beer getById(int id);

    Beer getByName(String name);

    void createBeer(Beer beer);

    void updateBeer(Beer beer);

    void deleteBeer(int id);
}
