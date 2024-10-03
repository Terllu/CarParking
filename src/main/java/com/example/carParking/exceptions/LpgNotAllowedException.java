package com.example.carParking.exceptions;

public class LpgNotAllowedException extends RuntimeException {
    public LpgNotAllowedException(String message) {
        super(message);
    }
}
