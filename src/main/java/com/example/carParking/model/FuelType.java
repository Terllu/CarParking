package com.example.carParking.model;


import com.fasterxml.jackson.annotation.JsonCreator;

public enum FuelType {
    ELECTRIC,
    LPG,
    DIESEL,
    PETROL;


    @JsonCreator
    public static FuelType fromValue(String value) {
        return FuelType.valueOf(value.toUpperCase());
    }
}
