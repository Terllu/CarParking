package com.example.carParking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cars")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
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

    @ManyToOne
    @JoinColumn(name = "parking_id")
    @JsonBackReference
    private ParkingEntity parking;

    @Override
    public String toString() {
        return "CarEntity{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", width=" + width +
                ", fuelType='" + fuelType + '\'' +
                ", parkingId=" + (parking != null ? parking.getId() : "null") +
                '}';
    }
}
