package com.example.carParking.service;

import com.example.carParking.exceptions.*;
import com.example.carParking.model.CarEntity;
import com.example.carParking.model.FuelType;
import com.example.carParking.model.ParkingEntity;
import com.example.carParking.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CarService {

    private static final Logger logger = LoggerFactory.getLogger(CarService.class);
    private final CarRepository carRepository;

    public CarService(final CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public CarEntity createCar(CarEntity carEntity) {
        logger.info("Creating car: {}", carEntity);
        carEntity.setParking(null);
        return carRepository.save(carEntity);
    }

    public Page<CarEntity> getAllCars(final Pageable pageable) {
        logger.info("Fetching all cars with pagination: {}", pageable);
        return carRepository.findAll(pageable);
    }

    public CarEntity updateCar(Long id, CarEntity carEntity) {
        logger.info("Updating car with ID: {}", id);

        return carRepository.findById(id).map(existingCar -> {
            existingCar.setBrand(carEntity.getBrand());
            existingCar.setModel(carEntity.getModel());
            existingCar.setWidth(carEntity.getWidth());
            existingCar.setFuelType(carEntity.getFuelType());
            if (existingCar.getParking() != null) {
                validateCarAddition(existingCar.getParking(), existingCar);
            }
            logger.info("Updated car details: {}", existingCar);
            return carRepository.save(existingCar);
        }).orElseThrow(() -> {
            logger.error("No car found with ID: {}", id);
            return new NoCarFoundException("Car with ID: " + id + " not found!");
        });
    }

    public CarEntity getCarById(Long id) {
        logger.info("Fetching car with ID: {}", id);
        return carRepository.findById(id).orElseThrow(() -> {
            logger.error("No car found with ID: {}", id);
            return new NoCarFoundException("Car with ID: " + id + " not found!");
        });
    }

    public void deleteCar(Long id) {
        CarEntity carEntity = carRepository.findById(id).orElseThrow(() -> {
            logger.error("Cannot delete. No car found with ID: {}", id);
            return new NoCarFoundException("Car with ID: " + id + " not found!");
        });

        if (carEntity.getParking() != null)
            throw new CarParkedException("First u need to delete car from parking!");

        logger.info("Deleting car with ID: {}", id);
        carRepository.deleteById(id);
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
            if (numberOfElectricCarsParked > parking.getNumberOfChargers()) {
                logger.error("No free chargers available in parking with ID: {}", parking.getId());
                throw new NoFreeChargersParkingException("There are no free chargers!");
            }
        }
    }
}
