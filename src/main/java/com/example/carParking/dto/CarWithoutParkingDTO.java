package com.example.carParking.dto;

import com.example.carParking.model.FuelType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarWithoutParkingDTO {

    private Long id;

    @NotNull(message = "Brand cannot be null")
    @Size(min = 1, max = 100, message = "Brand must be between 1 and 100 characters")
    private String brand;

    @NotNull(message = "Model cannot be null")
    @Size(min = 1, max = 100, message = "Model must be between 1 and 100 characters")
    private String model;

    @NotNull(message = "Width cannot be null")
    @Min(value = 0, message = "Width must be a non-negative number")
    private Double width;

    @NotNull(message = "Fuel type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private FuelType fuelType;
}
