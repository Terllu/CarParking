package com.example.carParking;

import com.example.carParking.dto.ParkingDTO;
import com.example.carParking.model.CarEntity;
import com.example.carParking.model.FuelType;
import com.example.carParking.model.ParkingEntity;

import java.util.Collections;

public final class TestDataUtil {

    // ParkingEntity

    public static ParkingEntity createParkingEntity() {
        return ParkingEntity.builder()
                .id(1L)
                .name("Test Parking")
                .address("123 Test Address")
                .totalSpaces(100)
                .numberOfChargers(10)
                .lpgAllowed(true)
                .widthOfParkingSpace(2.5)
                .cars(Collections.emptyList())
                .build();
    }

    public static ParkingEntity createParkingEntityWithLpgNotAllowed() {
        return ParkingEntity.builder()
                .id(1L)
                .name("Test Parking")
                .address("123 Test Address")
                .totalSpaces(100)
                .numberOfChargers(10)
                .lpgAllowed(false)
                .widthOfParkingSpace(2.5)
                .cars(Collections.emptyList())
                .build();
    }

    public static ParkingEntity createParkingEntityWithNoChargers() {
        return ParkingEntity.builder()
                .id(1L)
                .name("Test Parking")
                .address("123 Test Address")
                .totalSpaces(100)
                .numberOfChargers(0)
                .lpgAllowed(false)
                .widthOfParkingSpace(2.5)
                .cars(Collections.emptyList())
                .build();
    }

    public static ParkingEntity createUpdatedParkingEntity() {
        return ParkingEntity.builder()
                .id(2L)
                .name("Updated Parking")
                .address("456 Updated Address")
                .totalSpaces(150)
                .numberOfChargers(12)
                .lpgAllowed(false)
                .widthOfParkingSpace(3.0)
                .cars(Collections.emptyList())
                .build();
    }

    // CarEntity

    public static CarEntity createCarEntity() {
        ParkingEntity parkingEntity = createParkingEntity();

        return CarEntity.builder()
                .id(1L)
                .brand("Test Brand")
                .model("Test Model")
                .width(1.8)
                .fuelType(FuelType.ELECTRIC)
                .parking(parkingEntity)
                .build();
    }

    public static CarEntity createCarEntityWithoutParking() {

        return CarEntity.builder()
                .id(1L)
                .brand("Test Brand")
                .model("Test Model")
                .width(1.8)
                .fuelType(FuelType.LPG)
                .parking(null)
                .build();
    }

    public static CarEntity createCarEntityWithElectricMotor() {
        ParkingEntity parkingEntity = createParkingEntity();

        return CarEntity.builder()
                .id(2L)
                .brand("Different Brand")
                .model("Different Model")
                .width(2.0)
                .fuelType(FuelType.ELECTRIC)
                .parking(parkingEntity)
                .build();
    }

    public static CarEntity createCarEntityWithoutParkingWithElectricMotor() {

        return CarEntity.builder()
                .id(1L)
                .brand("Different Brand")
                .model("Different Model")
                .width(2.0)
                .fuelType(FuelType.ELECTRIC)
                .parking(null)
                .build();
    }

    public static CarEntity createCarEntityWithLPG() {
        ParkingEntity parkingEntity = createParkingEntity();

        return CarEntity.builder()
                .id(2L)
                .brand("Different Brand")
                .model("Different Model")
                .width(2.0)
                .fuelType(FuelType.LPG)
                .parking(parkingEntity)
                .build();
    }

    // ParkingDTO
    public static ParkingDTO createParkingDTO() {
        return ParkingDTO.builder()
                .id(1L)
                .name("Test Parking")
                .address("123 Test Address")
                .totalSpaces(100)
                .numberOfChargers(10)
                .lpgAllowed(true)
                .widthOfParkingSpace(2.5)
                .cars(Collections.emptyList())
                .build();
    }
}
