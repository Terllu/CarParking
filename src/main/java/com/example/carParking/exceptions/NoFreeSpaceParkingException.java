package com.example.carParking.exceptions;

public class NoFreeSpaceParkingException extends RuntimeException {
    public NoFreeSpaceParkingException(String message) {
        super(message);
    }
}
