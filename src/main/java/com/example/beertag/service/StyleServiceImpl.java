package com.example.beertag.service;

import com.example.beertag.models.Style;
import com.example.beertag.repository.BeerRepository;
import com.example.beertag.repository.StyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StyleServiceImpl implements StyleService {

    private StyleRepository styleRepository;

    @Autowired
    public StyleServiceImpl(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Override
    public List<Style> getAll() {
        return styleRepository.getAll();
    }

    @Override
    public Style getById(int id) {
        return null;
    }
}
