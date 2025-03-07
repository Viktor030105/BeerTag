package com.example.beertag.service;

import com.example.beertag.models.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(int id);

    User getByUsername(String username);

    void createUser(User user);

    void updateUser(User user);

    void deleteBeer(int id);

    void addBeerToWishList(int userId, int beerId);

    void removeFromWishList(int userId, int beerId);

}
