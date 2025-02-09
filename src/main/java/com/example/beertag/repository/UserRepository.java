package com.example.beertag.repository;

import com.example.beertag.models.User;

import java.util.List;

public interface UserRepository {

    List<User> getAll();

    User getById(int id);

    User getByName(String username);

    void createUser(User user);

    void updateUser(User user);


}
