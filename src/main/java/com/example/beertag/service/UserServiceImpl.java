package com.example.beertag.service;

import com.example.beertag.exeptions.DuplicateEntityException;
import com.example.beertag.exeptions.EntityNotFoundException;
import com.example.beertag.models.Beer;
import com.example.beertag.models.User;
import com.example.beertag.repository.BeerRepository;
import com.example.beertag.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final BeerRepository beerRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BeerRepository beerRepository) {
        this.userRepository = userRepository;
        this.beerRepository = beerRepository;
    }

    @Override
    public List<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByName(username);
    }

    @Override
    public void createUser(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByName(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        userRepository.createUser(user);
    }

    @Override
    public void updateUser(User user) {
        boolean duplicateExists = true;
        try {
            userRepository.getByName(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new DuplicateEntityException("User", "username", user.getUsername());
        }

        userRepository.updateUser(user);
    }

    @Override
    public void deleteBeer(int id) {
        userRepository.deleteUser(id);
    }

    @Override
    public void addBeerToWishList(int userId, int beerId) {
        User user = userRepository.getById(userId);
        if (user.getWishList().stream().anyMatch(b -> b.getId() == beerId)) {
            return;
        }
        Beer beer = beerRepository.getById(beerId);
        user.getWishList().add(beer);
        userRepository.updateUser(user);
    }

    @Override
    public void removeFromWishList(int userId, int beerId) {

        User user = userRepository.getById(userId);
        if (user.getWishList().stream().noneMatch(b -> b.getId() == beerId)) {
            throw new EntityNotFoundException("Beer", beerId);
        }
        user.getWishList().removeIf(b -> b.getId() == beerId);
        userRepository.updateUser(user);
    }
}
