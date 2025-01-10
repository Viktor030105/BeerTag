package com.example.beertag.repository;

import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Beer;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BeerRepositoryImpl implements BeerRepository{

    private final List<Beer> beers;

    public BeerRepositoryImpl(StyleRepository styleRepository) {
        beers = new ArrayList<>();
        Beer beer = new Beer(1, "Glarus English Ale", 4.6);
        beer.setStyle(styleRepository.getById(1));
        beers.add(beer);

        beer = new Beer(2, "Rhombus Porter", 5.0);
        beer.setStyle(styleRepository.getById(2));
        beers.add(beer);

        beer = new Beer(3, "Opasen Char", 6.6);
        beer.setStyle(styleRepository.getById(3));
        beers.add(beer);
    }

    @Override
    public List<Beer> getAllBeers(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        List<Beer> result = beers;
        result = filterByName(result, name);
        result = filterByAbv(result, minAbv, maxAbv);
        result = filterByStyle(result, styleId);
        result = sortBy(result, sortBy);
        result = order(result, sortOrder);
        return result;
    }

    @Override
    public Beer getById(int id){
        return beers.stream()
                .filter(beer -> beer.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Beer", id));
    }

    @Override
    public Beer getByName(String name){
        return beers.stream()
                .filter(beer -> beer.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Beer", "name", name));
    }

    @Override
    public void createBeer(Beer beer){
        int nextId = beers.size() + 1;
        beer.setId(nextId);
        beers.add(beer);
    }

    @Override
    public void updateBeer(Beer beer){
        Beer beerToUpdate = getById(beer.getId());
        beerToUpdate.setName(beer.getName());
        beerToUpdate.setAbv(beer.getAbv());
        beerToUpdate.setStyle(beer.getStyle());
    }

    @Override
    public void deleteBeer(int id){
        Beer beerToDelete = getById(id);
        beers.remove(beerToDelete);
    }

    private static List<Beer> filterByName(List<Beer> beers, String name) {
        if (name != null && !name.isEmpty()) {
            beers = beers.stream()
                    .filter(beer -> containsIgnoreCase(beer.getName(), name))
                    .collect(Collectors.toList());
        }
        return beers;
    }

    private static List<Beer> filterByAbv(List<Beer> beers, Double minAbv, Double maxAbv) {
        if (minAbv != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getAbv() >= minAbv)
                    .collect(Collectors.toList());
        }

        if (maxAbv != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getAbv() <= maxAbv)
                    .collect(Collectors.toList());
        }

        return beers;
    }

    private static List<Beer> filterByStyle(List<Beer> beers, Integer styleId) {
        if (styleId != null) {
            beers = beers.stream()
                    .filter(beer -> beer.getStyle().getId() == styleId)
                    .collect(Collectors.toList());
        }
        return beers;
    }

    private static List<Beer> sortBy(List<Beer> beers, String sortBy) {
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "name":
                    beers.sort(Comparator.comparing(Beer::getName));
                    break;
                case "abv":
                    beers.sort(Comparator.comparing(Beer::getAbv));
                case "style":
                    beers.sort(Comparator.comparing(beer -> beer.getStyle().getName()));
                    break;
            }
        }
        return beers;
    }

    private static List<Beer> order(List<Beer> beers, String order) {
        if (order != null && !order.isEmpty()) {
            if (order.equals("desc")) {
                Collections.reverse(beers);
            }
        }
        return beers;
    }

    private static boolean containsIgnoreCase(String value, String sequence) {
        return value.toLowerCase().contains(sequence.toLowerCase());
    }
}
