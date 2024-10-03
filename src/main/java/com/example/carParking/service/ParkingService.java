package com.example.carParking.service;

import com.example.carParking.exceptions.*;
import com.example.carParking.model.CarEntity;
import com.example.carParking.model.FuelType;
import com.example.carParking.model.ParkingEntity;
import com.example.carParking.repository.CarRepository;
import com.example.carParking.repository.ParkingRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class ParkingService {

    private static final Logger logger = LoggerFactory.getLogger(ParkingService.class);

    private final ParkingRepository parkingRepository;

    private final CarRepository carRepository;

    public ParkingService(final ParkingRepository parkingRepository, CarRepository carRepository) {
        this.parkingRepository = parkingRepository;
        this.carRepository = carRepository;
    }

    public ParkingEntity createParking(ParkingEntity parkingEntity) {
        logger.info("Creating new parking with details: {}", parkingEntity);
        ParkingEntity parking = parkingRepository.save(parkingEntity);

        logger.info("Successfully created parking with ID: {}", parking.getId());
        return parking;
    }

    public Page<ParkingEntity> getAllParkings(final Pageable pageable) {
        logger.info("Fetching all parkings with pagination: {}", pageable);
        Page<ParkingEntity> parkings = parkingRepository.findAll(pageable);
        logger.info("Successfully retrieved {} parkings", parkings.getTotalElements());
        return parkings;
    }


    public ParkingEntity getParkingById(Long id) {
        logger.info("Fetching parking with ID: {}", id);
        ParkingEntity parkingEntity = parkingRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Parking with ID: {} does not exist", id);
                    return new NoParkingFoundException("Parking with ID: " + id + " does not exist!");
                });
        logger.info("Successfully retrieved parking with ID: {}", id);
        return parkingEntity;
    }

    public ParkingEntity updateParking(Long id, ParkingEntity parkingEntity) {
        logger.info("Updating parking with ID: {}", id);

        return parkingRepository.findById(id)
                .map(existingParking -> {
                    existingParking.setName(parkingEntity.getName());
                    existingParking.setAddress(parkingEntity.getAddress());
                    existingParking.setTotalSpaces(parkingEntity.getTotalSpaces());
                    existingParking.setNumberOfChargers(parkingEntity.getNumberOfChargers());
                    existingParking.setWidthOfParkingSpace(parkingEntity.getWidthOfParkingSpace());
                    existingParking.setLpgAllowed(parkingEntity.isLpgAllowed());
                    if (existingParking.getCars() != null) {
                        for (CarEntity car: existingParking.getCars()){
                            validateCarAddition(existingParking, car);
                        }
                    }
                    ParkingEntity savedParking = parkingRepository.save(existingParking);
                    logger.info("Parking with ID: {} updated successfully", id);
                    return savedParking;
                })
                .orElseThrow(() -> {
                    logger.error("Parking with ID: {} not found for update", id);
                    return new NoParkingFoundException("Parking with ID: " + id + " does not exist!");
                });
    }

    public void deleteParking(Long id) {
        logger.info("Deleting parking with ID: {}", id);

        ParkingEntity parkingEntity = parkingRepository.findById(id).orElseThrow(()-> {
            logger.error("Parking with ID: {} does not exist for deletion", id);
            return new NoParkingFoundException("Parking with ID: " + id + " does not exist!");
        });

        if (parkingEntity.getCars() != null) {
            for (CarEntity car : parkingEntity.getCars()) {
                car.setParking(null);
            }
        }

        parkingRepository.deleteById(id);
        logger.info("Successfully deleted parking with ID: {}", id);
    }

    public ParkingEntity addCarToParking(Long parkingId, Long carId) {
        logger.info("Adding car to parking with ID: {} with car details: {}", parkingId, carId);

        ParkingEntity parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> {
                    logger.error("Parking with ID: {} does not exist for adding car", parkingId);
                    return new NoParkingFoundException("Parking with ID: " + parkingId + " does not exist!");
                });

        CarEntity carEntity = carRepository.findById(carId).orElseThrow(() -> {
            logger.error("Car with ID: {} does not exist", carId);
            return new NoCarFoundException("Car with ID: " + carId + " not found!");
        });

        validateCarAddition(parking, carEntity);
        if (parking.getCars().size() >= parking.getTotalSpaces()) {
            logger.error("No free space available in parking with ID: {}", parking.getId());
            throw new NoFreeSpaceParkingException("You can't park here, parking is full");
        }

        List<CarEntity> cars = new ArrayList<>(parking.getCars());

        carEntity.setParking(parking);
        cars.add(carEntity);
        parking.setCars(cars);

        ParkingEntity updatedParking = parkingRepository.save(parking);
        logger.info("Successfully added car with ID: {} to parking with ID: {}", carEntity.getId(), parkingId);

        return updatedParking;
    }

    public ParkingEntity deleteCarFromParking(Long parkingId, Long carId) {
        logger.info("Deleting car from parking with ID: {} with car details: {}", parkingId, carId);

        ParkingEntity parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> {
                    logger.error("Parking with ID: {} does not exist for deleting car", parkingId);
                    return new NoParkingFoundException("Parking with ID: " + parkingId + " does not exist!");
                });

        CarEntity carEntity = carRepository.findById(carId).orElseThrow(() -> {
            logger.error("Car with ID: {} does not exist", carId);
            return new NoCarFoundException("Car with ID: " + carId + " not found!");
        });

        carEntity.setParking(null);
        parking.getCars().remove(carEntity);

        ParkingEntity updatedParking = parkingRepository.save(parking);

        logger.info("Successfully deleted car with ID: {} to parking with ID: {}", carEntity.getId(), parkingId);

        return updatedParking;
    }

    private void validateCarAddition(ParkingEntity parking, CarEntity car) {
        if (car.getFuelType().equals(FuelType.LPG) && !parking.isLpgAllowed() ) {
            logger.error("LPG cars are not allowed in parking with ID: {}", parking.getId());
            throw new LpgNotAllowedException("You can't park LPG car here!");
        }

        if (car.getWidth() > parking.getWidthOfParkingSpace()) {
            logger.error("Car with width {} is too wide for parking with ID: {}", car.getWidth(), parking.getId());
            throw new CarTooWideException("Your car is too wide to park here!");
        }

        if (car.getFuelType().equals(FuelType.ELECTRIC)) {
            long numberOfElectricCarsParked = parking.getCars().stream()
                    .filter(c -> c.getFuelType().equals(FuelType.ELECTRIC))
                    .count();
            if (parking.getNumberOfChargers() <= numberOfElectricCarsParked) {
                logger.error("No free chargers available in parking with ID: {}", parking.getId());
                throw new NoFreeChargersParkingException("You can't park Electric car here, all chargers are occupied!");
            }
        }
    }
}