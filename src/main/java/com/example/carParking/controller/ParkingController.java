package com.example.carParking.controller;

import com.example.carParking.dto.ParkingDTO;
import com.example.carParking.dto.ParkingWithoutCarsDTO;
import com.example.carParking.model.ParkingEntity;
import com.example.carParking.service.ParkingService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkings")
public class ParkingController {

    private static final Logger logger = LoggerFactory.getLogger(ParkingController.class);
    private final ParkingService parkingService;
    private final ModelMapper modelMapper;

    public ParkingController(ParkingService parkingService, ModelMapper modelMapper) {
        this.parkingService = parkingService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<ParkingWithoutCarsDTO> createParking(@Valid @RequestBody ParkingWithoutCarsDTO parkingDTO) {
        logger.info("Creating a new parking: {}", parkingDTO.getName());

        ParkingEntity parkingRequest = modelMapper.map(parkingDTO, ParkingEntity.class);
        ParkingEntity createdParking = parkingService.createParking(parkingRequest);
        ParkingWithoutCarsDTO parkingResponse = modelMapper.map(createdParking, ParkingWithoutCarsDTO.class);
        logger.info("Successfully created parking with ID: {}", createdParking.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingResponse);
    }

    @GetMapping
    public ResponseEntity<Page<ParkingDTO>> getAllParkings(@PageableDefault Pageable pageable) {
        logger.info("Fetching all parkings with pageable: {}", pageable);
        Page<ParkingEntity> parkingPage = parkingService.getAllParkings(pageable);
        Page<ParkingDTO> responsePage = parkingPage.map(parking -> modelMapper.map(parking, ParkingDTO.class));
        logger.info("Retrieved {} parkings", responsePage.getTotalElements());
        return ResponseEntity.ok(responsePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingDTO> getParkingById(@PathVariable Long id) {
        logger.info("Fetching parking with ID: {}", id);
        ParkingEntity parkingRequest = parkingService.getParkingById(id);
        ParkingDTO parkingResponse = modelMapper.map(parkingRequest, ParkingDTO.class);
        return ResponseEntity.ok(parkingResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ParkingWithoutCarsDTO> updateParking(@PathVariable Long id, @Valid @RequestBody ParkingWithoutCarsDTO parkingDTO) {
        logger.info("Updating parking with ID: {}", id);
        ParkingEntity parkingRequest = modelMapper.map(parkingDTO, ParkingEntity.class);
        ParkingEntity updatedParking = parkingService.updateParking(id, parkingRequest);
        ParkingWithoutCarsDTO parkingResponse = modelMapper.map(updatedParking, ParkingWithoutCarsDTO.class);
        logger.info("Successfully updated parking with ID: {}", id);
        return ResponseEntity.ok(parkingResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParking(@PathVariable Long id) {
        logger.info("Deleting parking with ID: {}", id);
        parkingService.deleteParking(id);
        logger.info("Successfully deleted parking with ID: {}", id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{parkingId}/addCar/{carId}")
    public ResponseEntity<ParkingDTO> addCarToParking(@PathVariable Long parkingId, @Valid @PathVariable Long carId) {
        logger.info("Adding car to parking with ID: {}", parkingId);
        ParkingEntity updatedParking = parkingService.addCarToParking(parkingId, carId);
        ParkingDTO parkingResponse = modelMapper.map(updatedParking, ParkingDTO.class);
        logger.info("Successfully added car to parking with ID: {}", parkingId);
        return ResponseEntity.ok(parkingResponse);
    }

    @DeleteMapping("/{parkingId}/deleteCar/{carId}")
    public ResponseEntity<ParkingDTO> deleteCarFromParking(@PathVariable Long parkingId, @Valid @PathVariable Long carId) {
        logger.info("Deleting car from parking with ID: {}", parkingId);
        ParkingEntity updatedParking = parkingService.deleteCarFromParking(parkingId, carId);
        ParkingDTO parkingResponse = modelMapper.map(updatedParking, ParkingDTO.class);
        logger.info("Successfully deleted car from parking with ID: {}", parkingId);
        return ResponseEntity.ok(parkingResponse);
    }
}
