package com.example.carParking.exceptions;

public class NoParkingFoundException extends RuntimeException {
    public NoParkingFoundException(String message) {
        super(message);
    }
}
