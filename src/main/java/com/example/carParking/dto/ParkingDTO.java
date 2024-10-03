package com.example.carParking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingDTO {

    private Long id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Address cannot be null")
    @Size(min = 1, max = 100, message = "Address must be between 1 and 100 characters")
    private String address;

    @NotNull(message = "Total spaces cannot be null")
    @Min(value = 0, message = "Total spaces must be a non-negative number")
    private Integer totalSpaces;

    @Min(value = 0, message = "Number of chargers must be a non-negative number")
    private Integer numberOfChargers;

    private boolean lpgAllowed;

    @Min(value = 0, message = "Width of parking space must be a non-negative number")
    private Double widthOfParkingSpace;

    private List<CarDTO> cars;
}
