package com.example.beertag.repository;

import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.Style;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StyleRepositoryImpl implements StyleRepository{

    private final List<Style> styles;

    public StyleRepositoryImpl() {
        this.styles = new ArrayList<>();

        styles.add(new Style(1, "Special Ale"));
        styles.add(new Style(2, "English Porter"));
        styles.add(new Style(3, "Indian Pale Ale"));
    }

    @Override
    public Style getById(int id) {
        return styles.stream()
                .filter(style -> style.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("Style", id));
    }

    @Override
    public List<Style> getAll(){
        return styles;
    }
}
