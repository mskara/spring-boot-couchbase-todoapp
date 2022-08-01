package com.mskara.todoapp.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super(username + " is not found!");
    }
}
