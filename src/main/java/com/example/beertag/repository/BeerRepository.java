package com.example.beertag.repository;

import com.example.beertag.models.Beer;
import com.example.beertag.models.FilterOptions;

import java.util.List;

public interface BeerRepository {

    List<Beer> getAll(FilterOptions filterOptions);

    Beer getById(int id);

    Beer getByName(String name);

    void createBeer(Beer beer);

    void updateBeer(Beer beer);

    void deleteBeer(int id);
}
