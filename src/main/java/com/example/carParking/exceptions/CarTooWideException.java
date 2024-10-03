package com.example.carParking.exceptions;

public class CarTooWideException extends RuntimeException {
    public CarTooWideException(String message) {
        super(message);
    }
}
