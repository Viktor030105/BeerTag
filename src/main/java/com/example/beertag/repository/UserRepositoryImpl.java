package com.example.beertag.repository;

import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users;

    public UserRepositoryImpl() {
        users = new ArrayList<>();
        users.add(new User(1, "Gosho", "12345", true));
        users.add(new User(2, "Ivan","12345", false));
        users.add(new User(3, "Pesho", "12345", false));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User getById(int id) {
        return getAll().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("User", id));
    }

    @Override
    public User getByUsername(String username) {
        return getAll().stream()
                .filter(user -> user.getUsername() == username)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundExeption("User", "username", username));
    }
}
