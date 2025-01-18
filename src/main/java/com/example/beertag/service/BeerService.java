package com.example.beertag.service;

import com.example.beertag.models.Beer;
import com.example.beertag.models.FilterOptions;
import com.example.beertag.models.User;

import java.util.List;

public interface BeerService {

    List<Beer> getAll(FilterOptions filterOptions);

    Beer getById(int id);

    void createBeer(Beer beer, User user);

    void updateBeer(Beer beer, User user);

    void deleteBeer(int id, User user);
}
