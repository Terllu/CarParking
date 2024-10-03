package com.example.carParking.exceptions;

public class NoFreeChargersParkingException extends RuntimeException {
    public NoFreeChargersParkingException(String message) {
        super(message);
    }
}
