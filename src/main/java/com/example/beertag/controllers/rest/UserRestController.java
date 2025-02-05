package com.example.beertag.controllers.rest;

import com.example.beertag.exeptions.UnauthorizedOperationException;
import com.example.beertag.helpers.AuthenticationHelper;
import com.example.beertag.models.Beer;
import com.example.beertag.models.User;
import com.example.beertag.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    public static final String ERROR_MESSAGE = "You are not authorized to browse user information.";
    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public UserRestController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.userService = userService;
        this.authenticationHelper = authenticationHelper;
    }

    @GetMapping
    public List<User> getAllUsers(@RequestHeader HttpHeaders headers){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            if (!user.isAdmin()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ERROR_MESSAGE);
            }
            return userService.getAll();
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @GetMapping("/{id}/wish-list")
    public List<Beer> getWishList(@RequestHeader HttpHeaders headers, @PathVariable int id){
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            return new ArrayList<>(userService.getById(id).getWishList());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("/{id}/wish-list/{beerId}")
    public void addToWishList(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id,
            @PathVariable int beerId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            userService.addBeerToWishList(id, beerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @DeleteMapping("/{id}/wish-list/{beerId}")
    public void removeFromWishList(
            @RequestHeader HttpHeaders headers,
            @PathVariable int id,
            @PathVariable int beerId) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, user);
            userService.removeFromWishList(id, beerId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.isAdmin() && executingUser.getId() != targetUserId) {
            throw new UnauthorizedOperationException(ERROR_MESSAGE);
        }
    }
}
