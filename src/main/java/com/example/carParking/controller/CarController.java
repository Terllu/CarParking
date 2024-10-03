package com.example.carParking.controller;

import com.example.carParking.dto.CarDTO;
import com.example.carParking.dto.CarWithoutParkingDTO;
import com.example.carParking.model.CarEntity;
import com.example.carParking.service.CarService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;
    private final ModelMapper modelMapper;

    public CarController(CarService carService, ModelMapper modelMapper) {
        this.carService = carService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<CarWithoutParkingDTO> createCar(@Valid @RequestBody CarWithoutParkingDTO carDTO) {
        CarEntity carRequest = modelMapper.map(carDTO, CarEntity.class);
        CarEntity createdCar = carService.createCar(carRequest);
        CarWithoutParkingDTO carResponse = modelMapper.map(createdCar, CarWithoutParkingDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(carResponse);
    }

    @GetMapping
    public ResponseEntity<Page<CarDTO>> getAllCars(@PageableDefault Pageable pageable) {
        Page<CarEntity> carRequest = carService.getAllCars(pageable);
        Page<CarDTO> carResponse = carRequest.map(car -> modelMapper.map(car, CarDTO.class));

        return ResponseEntity.ok(carResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
        CarEntity carRequest = carService.getCarById(id);
        CarDTO carResponse = modelMapper.map(carRequest, CarDTO.class);

        return ResponseEntity.ok(carResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarWithoutParkingDTO> updateCar(@PathVariable Long id, @Valid @RequestBody CarWithoutParkingDTO carDTO) {
        CarEntity carRequest = modelMapper.map(carDTO, CarEntity.class);
        CarEntity updatedCar = carService.updateCar(id, carRequest);

        CarWithoutParkingDTO carResponse = modelMapper.map(updatedCar, CarWithoutParkingDTO.class);
        return ResponseEntity.ok(carResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}
