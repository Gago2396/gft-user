package com.gfttraining.users.exceptions;

public class NoUsersWithThatNameException extends RuntimeException {
    public NoUsersWithThatNameException(String message) {
        super(message);
    }
}
