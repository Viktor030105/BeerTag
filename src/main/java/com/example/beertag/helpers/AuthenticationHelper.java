package com.example.beertag.helpers;

import com.example.beertag.exeptions.EntityNotFoundExeption;
import com.example.beertag.models.User;
import com.example.beertag.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthenticationHelper {

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "The requested user is not authenticated");
        }

        try {
            String username = headers.getFirst(HttpHeaders.AUTHORIZATION);
            return userService.getByUsername(username);
        } catch (EntityNotFoundExeption ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username");
        }
    }
}
