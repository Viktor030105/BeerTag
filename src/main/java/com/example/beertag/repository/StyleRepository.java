package com.example.beertag.repository;

import com.example.beertag.models.Style;
import com.example.beertag.models.User;

import java.util.List;


public interface StyleRepository {

    List<Style> getAll();

    Style getById(int id);
}
