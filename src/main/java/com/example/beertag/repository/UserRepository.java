package com.example.beertag.repository;

import com.example.beertag.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);


}
