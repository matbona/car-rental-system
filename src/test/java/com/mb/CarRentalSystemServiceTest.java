package com.mb;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CarRentalSystemServiceTest {

    @Test
    void shouldReserveCarTypeWhenAvailable() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 1,
                CarType.SUV, 1,
                CarType.VAN, 1
        ));

        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);

        // When
        CarReservation carReservation = carRentalSystemService.reserveCar(CarType.SEDAN, start, 2);

        // Then
        assertThat(carReservation.id()).isNotNull();
        assertThat(carReservation.carType()).isEqualTo(CarType.SEDAN);
        assertThat(carReservation.carReservationTimeSlot().startingDateTime()).isEqualTo(start);
        assertThat(carReservation.carReservationTimeSlot().endingDateTime()).isEqualTo(LocalDateTime.of(2026, 8, 3, 10, 0));
        assertThat(carRentalSystemService.getCarReservations()).containsExactly(carReservation);
    }

    @Test
    void shouldRejectReservationWhenOnlyCarOfRequestedTypeIsAlreadyReservedInOverlappingPeriod() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 1,
                CarType.SUV, 1,
                CarType.VAN, 1
        ));

        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);
        carRentalSystemService.reserveCar(CarType.SUV, start, 3);

        // When
        // Then
        assertThatThrownBy(() -> carRentalSystemService.reserveCar(CarType.SUV, start.plusDays(1), 2))
                .isInstanceOf(CarTypeNotExistException.class);
    }

    @Test
    void shouldAllowTwoOverlappingSedanReservationsWhenTwoSedansExist() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 2,
                CarType.SUV, 1,
                CarType.VAN, 1
        ));

        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);

        // When
        CarReservation first = carRentalSystemService.reserveCar(CarType.SEDAN, start, 2);
        CarReservation second = carRentalSystemService.reserveCar(CarType.SEDAN, start.plusHours(1), 2);

        //Then
        assertThat(carRentalSystemService.getCarReservations()).containsExactly(first, second);
        assertThat(carRentalSystemService.getNumberOfAvailableCars(CarType.SEDAN, start.plusHours(2), 1)).isZero();
    }

    @Test
    void shouldRejectThirdOverlappingSedanReservationWhenOnlyTwoSedansExist() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 2,
                CarType.SUV, 1,
                CarType.VAN, 1
        ));

        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);

        // When
        carRentalSystemService.reserveCar(CarType.SEDAN, start, 2);
        carRentalSystemService.reserveCar(CarType.SEDAN, start.plusHours(1), 2);

        // Then
        assertThatThrownBy(() -> carRentalSystemService.reserveCar(CarType.SEDAN, start.plusHours(2), 1))
                .isInstanceOf(CarTypeNotExistException.class);
    }

    @Test
    void shouldAllowReservationStartingExactlyWhenPreviousReservationEnds() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 1,
                CarType.SUV, 1,
                CarType.VAN, 1
        ));

        LocalDateTime firstStart = LocalDateTime.of(2026, 8, 1, 10, 0);
        LocalDateTime secondStart = LocalDateTime.of(2026, 8, 3, 10, 0);

        // When
        carRentalSystemService.reserveCar(CarType.VAN, firstStart, 2);
        CarReservation secondReservation = carRentalSystemService.reserveCar(CarType.VAN, secondStart, 1);

        // Then
        assertThat(secondReservation.carReservationTimeSlot().startingDateTime()).isEqualTo(secondStart);
        assertThat(secondReservation.carReservationTimeSlot().endingDateTime()).isEqualTo(LocalDateTime.of(2026, 8, 4, 10, 0));
    }

    @Test
    void shouldReturnAvailableCarsForGivenPeriod() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 2,
                CarType.SUV, 1,
                CarType.VAN, 1
        ));

        // When
        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);

        carRentalSystemService.reserveCar(CarType.SEDAN, start, 2);

        int availableCars = carRentalSystemService.getNumberOfAvailableCars(CarType.SEDAN, start.plusHours(1), 1);

        // Then
        assertThat(availableCars).isEqualTo(1);
    }

    @Test
    void shouldRejectReservationWhenFleetHasNoCarsOfRequestedType() {
        // Given
        CarRentalSystemService carRentalSystemService = new CarRentalSystemService(Map.of(
                CarType.SEDAN, 1
        ));

        // When
        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);

        // Then
        assertThatThrownBy(() -> carRentalSystemService.reserveCar(CarType.VAN, start, 1))
                .isInstanceOf(CarTypeNotExistException.class);
    }

}
