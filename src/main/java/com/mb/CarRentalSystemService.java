package com.mb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CarRentalSystemService {

    private final Map<CarType, Integer> carFleet;
    private final List<CarReservation> carReservations;

    public CarRentalSystemService(Map<CarType, Integer> carFleet) {
        Objects.requireNonNull(carFleet, "fleetSize must not be null");
        validateCarFleetSize(carFleet);

        this.carFleet = Map.copyOf(carFleet);
        this.carReservations = new ArrayList<>();
    }

    public synchronized CarReservation reserveCar(CarType carType, LocalDateTime startDateTime, int days) {
        Objects.requireNonNull(carType, "carType must not be null");

        CarReservationTimeSlot carReservationTimeSlot = CarReservationTimeSlot.createCarReservationTimeSlot(startDateTime, days);

        if (!isReservationPossible(carType, carReservationTimeSlot)) {
            throw new CarTypeNotExistException("Given Car Type does not exist");
        }

        CarReservation carReservation = CarReservation.createCarReservation(carType, carReservationTimeSlot);
        carReservations.add(carReservation);

        return carReservation;
    }

    public synchronized int getNumberOfAvailableCars(CarType carType, LocalDateTime startDateTime, int days) {
        Objects.requireNonNull(carType, "carType must not be null");

        CarReservationTimeSlot carReservationTimeSlot = CarReservationTimeSlot.createCarReservationTimeSlot(startDateTime, days);

        int totalCarsOfType = carFleet.getOrDefault(carType, 0);
        long reservedCarsOfType = getNumberOfOverlappingReservations(carType, carReservationTimeSlot);

        return Math.max(0, totalCarsOfType - (int) reservedCarsOfType);
    }

    public synchronized List<CarReservation> getCarReservations() {
        return List.copyOf(this.carReservations);
    }

    private boolean isReservationPossible(CarType carType, CarReservationTimeSlot carReservationTimeSlot) {
        int totalCarsOfType = carFleet.getOrDefault(carType, 0);
        long reservedCarsOfType = getNumberOfOverlappingReservations(carType, carReservationTimeSlot);

        return reservedCarsOfType < totalCarsOfType;
    }

    private long getNumberOfOverlappingReservations(CarType carType, CarReservationTimeSlot carReservationTimeSlot) {
        return carReservations.stream()
                .filter(carReservation -> carReservation.isCarReservationForGivenCarType(carType))
                .filter(carReservation -> carReservation.isCarReservationTimeSlotOverlappingWithAnotherTimeSlot(carReservationTimeSlot))
                .count();
    }

    private static void validateCarFleetSize(Map<CarType, Integer> fleetSize) {
        fleetSize.forEach((carType, count) -> {
            Objects.requireNonNull(carType, "carType in fleetSize must not be null");
            Objects.requireNonNull(count, "car count must not be null");

            if (count < 0) {
                throw new IllegalArgumentException("Car count must not be negative");
            }
        });
    }
}
