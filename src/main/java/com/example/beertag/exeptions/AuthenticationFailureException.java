package com.example.beertag.exeptions;

public class AuthenticationFailureException extends RuntimeException{

    public AuthenticationFailureException(String message) {
        super(message);
    }
}
