package com.example.carParking;

import com.example.carParking.exceptions.*;
import com.example.carParking.model.CarEntity;
import com.example.carParking.model.ParkingEntity;
import com.example.carParking.repository.CarRepository;
import com.example.carParking.repository.ParkingRepository;
import com.example.carParking.service.ParkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceUnitTest {

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private ParkingService parkingService;

    private ParkingEntity parkingEntity;

    @BeforeEach
    void setUp() {
        //given
        parkingEntity = TestDataUtil.createParkingEntity();

    }

    @Test
    void testCreateParking_Success() {
        //given
        when(parkingRepository.save(parkingEntity)).thenReturn(parkingEntity);

        // when
        ParkingEntity result = parkingService.createParking(parkingEntity);

        // then
        assertNotNull(result, "ParkingEntity should not be null");
        assertEquals(parkingEntity.getId(), result.getId(), "The ID of the created parking should be: " + result.getId());
        assertEquals(parkingEntity.getName(), result.getName(), "The name of the created parking should be: " + result.getName());
        assertEquals(parkingEntity.getAddress(), result.getAddress(), "The address of the created parking should be: " + result.getAddress());
        assertEquals(parkingEntity.getTotalSpaces(), result.getTotalSpaces(), "The total space of the created parking should be: " + result.getTotalSpaces());
        assertEquals(parkingEntity.getNumberOfChargers(), result.getNumberOfChargers(), "The number of chargers of the created parking should be: " + result.getNumberOfChargers());
        assertEquals(parkingEntity.isLpgAllowed(), result.isLpgAllowed(), "The LPG of the created parking should be: " + result.isLpgAllowed());
        assertEquals(parkingEntity.getWidthOfParkingSpace(), result.getWidthOfParkingSpace(), "The width of parking space of the created parking should be: " + result.getWidthOfParkingSpace());
        assertEquals(parkingEntity.getCars(), result.getCars(), "The list of cars of the created parking should be: " + result.getCars());
        verify(parkingRepository, times(1)).save(parkingEntity);
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testGetParkingById_Success() {
        //given
        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));

        // when
        ParkingEntity result = parkingService.getParkingById(parkingEntity.getId());

        // then
        assertNotNull(result, "ParkingEntity should not be null");
        assertEquals(parkingEntity.getId(), result.getId(), "The ID of the found parking should be 1L");
        verify(parkingRepository, times(1)).findById(parkingEntity.getId());
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testGetParkingById_NotFound() {
        //given
        when(parkingRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(NoParkingFoundException.class, () -> parkingService.getParkingById(1L));

        // then
        assertEquals("Parking with ID: 1 does not exist!", exception.getMessage(), "Should be NoParkingFoundException");
        verify(parkingRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testGetAllParkings() {
        // given
        Page<ParkingEntity> parkingPage = new PageImpl<>(Collections.singletonList(parkingEntity));

        Pageable pageable = Pageable.unpaged();
        when(parkingRepository.findAll(pageable)).thenReturn(parkingPage);

        // when
        Page<ParkingEntity> result = parkingService.getAllParkings(pageable);

        // then
        assertNotNull(result, "ParkingEntity should not be null");
        assertEquals(1, result.getTotalElements());
        assertEquals(parkingEntity.getId(), result.getContent().get(0).getId(), "The ID of the created parking should be: " + result.getContent().get(0).getId());
        verify(parkingRepository, times(1)).findAll(pageable);
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testUpdateParking_Success() {
        //given
        ParkingEntity updatedParkingEntity = TestDataUtil.createParkingEntity();
        updatedParkingEntity.setName("Updated Test Name");

        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));
        when(parkingRepository.save(parkingEntity)).thenReturn(updatedParkingEntity);

        //when
        ParkingEntity result = parkingService.updateParking(updatedParkingEntity.getId(), updatedParkingEntity);

        //then
        assertNotNull(result, "ParkingEntity should not be null");
        assertEquals(updatedParkingEntity.getId(), result.getId(), "The ID of the created parking should be: " + result.getId());
        assertEquals(updatedParkingEntity.getName(), result.getName(), "The name of the created parking should be: " + result.getName());
        assertEquals(updatedParkingEntity.getAddress(), result.getAddress(), "The address of the created parking should be: " + result.getAddress());
        assertEquals(updatedParkingEntity.getTotalSpaces(), result.getTotalSpaces(), "The total space of the created parking should be: " + result.getTotalSpaces());
        assertEquals(updatedParkingEntity.getNumberOfChargers(), result.getNumberOfChargers(), "The number of chargers of the created parking should be: " + result.getNumberOfChargers());
        assertEquals(updatedParkingEntity.isLpgAllowed(), result.isLpgAllowed(), "The LPG of the created parking should be: " + result.isLpgAllowed());
        assertEquals(updatedParkingEntity.getWidthOfParkingSpace(), result.getWidthOfParkingSpace(), "The width of parking space of the created parking should be: " + result.getWidthOfParkingSpace());
        assertEquals(updatedParkingEntity.getCars(), result.getCars(), "The list of cars of the created parking should be: " + result.getCars());
        verify(parkingRepository, times(1)).findById(parkingEntity.getId());
        verify(parkingRepository, times(1)).save(parkingEntity);
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testDeleteParking_Success() {
        // given
        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));
        doNothing().when(parkingRepository).deleteById(parkingEntity.getId());

        // when
        parkingService.deleteParking(parkingEntity.getId());

        // then
        verify(parkingRepository, times(1)).findById(parkingEntity.getId());
        verify(parkingRepository, times(1)).deleteById(parkingEntity.getId());
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testDeleteParking_NotFound() {
        // given
        when(parkingRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when
        NoParkingFoundException exception = assertThrows(NoParkingFoundException.class, () -> parkingService.deleteParking(parkingEntity.getId()));

        // then
        assertEquals("Parking with ID: 1 does not exist!", exception.getMessage(), "Exception message should match!");
        verifyNoMoreInteractions(parkingRepository);
    }


    @Test
    void testAddCarToParking_Success() {

        // given
        CarEntity car = TestDataUtil.createCarEntityWithoutParking();
        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));


        // when
        parkingService.addCarToParking(parkingEntity.getId(), car.getId());


        // then
        assertNotNull(parkingEntity, "ParkingEntity should not be null");
        assertEquals(car, parkingEntity.getCars().get(0), "Car should be in the parking");
        verify(parkingRepository, times(1)).save(parkingEntity);
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testAddCarToParking_ThrowLpgNotAllowedException() {

        // given
        CarEntity car = TestDataUtil.createCarEntityWithoutParking();
        ParkingEntity parking = TestDataUtil.createParkingEntityWithLpgNotAllowed();
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        // when
        LpgNotAllowedException exception = assertThrows(LpgNotAllowedException.class, () -> parkingService.addCarToParking(parking.getId(), car.getId()));

        // then
        assertNotNull(parking, "ParkingEntity should not be null");
        assertEquals("You can't park LPG car here!", exception.getMessage(), "Exception message should match!");
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testAddCarToParking_ThrowCarToWideException() {

        // given
        CarEntity car = TestDataUtil.createCarEntityWithoutParking();
        car.setWidth(50.0);
        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));


        // when
        CarTooWideException exception = assertThrows(CarTooWideException.class, () -> parkingService.addCarToParking(parkingEntity.getId(), car.getId()));

        // then
        assertNotNull(parkingEntity, "ParkingEntity should not be null");
        assertEquals("Your car is too wide to park here!", exception.getMessage(), "Exception message should match!");
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testAddCarToParking_ThrowNoFreeChargersParkingException() {

        // given
        CarEntity car = TestDataUtil.createCarEntityWithoutParkingWithElectricMotor();
        ParkingEntity parking = TestDataUtil.createParkingEntityWithNoChargers();
        when(parkingRepository.findById(parking.getId())).thenReturn(Optional.of(parking));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));

        // when
        NoFreeChargersParkingException exception = assertThrows(NoFreeChargersParkingException.class, () -> parkingService.addCarToParking(parking.getId(), car.getId()));

        // then
        assertNotNull(parking, "ParkingEntity should not be null");
        assertEquals("You can't park Electric car here, all chargers are occupied!", exception.getMessage(), "Exception message should match!");
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testAddCarToParking_ThrowNoFreeSpaceParkingException() {

        // given
        CarEntity car = TestDataUtil.createCarEntityWithoutParking();
        parkingEntity.setTotalSpaces(0);
        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));


        // when
        NoFreeSpaceParkingException exception = assertThrows(NoFreeSpaceParkingException.class, () -> parkingService.addCarToParking(parkingEntity.getId(), car.getId()));

        // then
        assertNotNull(parkingEntity, "ParkingEntity should not be null");
        assertEquals("You can't park here, parking is full", exception.getMessage(), "Exception message should match!");
        verifyNoMoreInteractions(parkingRepository);
    }

    @Test
    void testDeleteCarFromParking_Success() {

        // given
        CarEntity car = TestDataUtil.createCarEntity();
        when(parkingRepository.findById(parkingEntity.getId())).thenReturn(Optional.of(parkingEntity));
        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));


        // when
        parkingService.deleteCarFromParking(parkingEntity.getId(), car.getId());

        // then
        assertNotNull(parkingEntity, "ParkingEntity should not be null");
        assertFalse(parkingEntity.getCars().contains(car), "Car should not be in the parking");
        verify(parkingRepository, times(1)).save(parkingEntity);
        verifyNoMoreInteractions(parkingRepository);
    }

}