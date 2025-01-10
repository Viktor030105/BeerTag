package com.example.beertag.service;

import com.example.beertag.models.Style;

import java.util.List;

public interface StyleService {

    List<Style> getAll();

    Style getById(int id);
}
