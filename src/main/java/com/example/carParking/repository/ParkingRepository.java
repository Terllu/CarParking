package com.example.carParking.repository;

import com.example.carParking.model.ParkingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends JpaRepository<ParkingEntity, Long> {

    @EntityGraph(attributePaths = {"cars"})
    Page<ParkingEntity> findAll(Pageable pageable);
}
