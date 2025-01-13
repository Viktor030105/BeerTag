package com.example.beertag.service;

import com.example.beertag.models.Beer;
import com.example.beertag.models.User;

import java.util.List;

public interface BeerService {

    List<Beer> getAllBeers(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder);

    Beer getById(int id);

    void createBeer(Beer beer);

    void updateBeer(Beer beer, User user);

    void deleteBeer(int id, User user);
}
