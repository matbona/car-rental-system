package com.mb;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CarRentalSystemService {

    private final EnumMap<CarType, Integer> carFleet;
    private final List<CarReservation> carReservations;

    public CarRentalSystemService(EnumMap<CarType, Integer> carFleet) {
        this.carFleet = carFleet;
        this.carReservations = new ArrayList<>();
    }

    public CarReservation reserveCar(CarType carType, LocalDateTime startDateTime, int days) {
        CarReservationTimeSlot carReservationTimeSlot = CarReservationTimeSlot.createCarReservationTimeSlot(startDateTime, days);

        if (!isReservationPossible(carType, carReservationTimeSlot)) {
            throw new CarTypeNotExistException("Given Car Type does not exist");
        }

        CarReservation carReservation = CarReservation.createCarReservation(carType, carReservationTimeSlot);
        carReservations.add(carReservation);

        return carReservation;
    }

    public int getNumberOfAvailableCars(CarType carType, LocalDateTime startDateTime, int days) {
        CarReservationTimeSlot carReservationTimeSlot = CarReservationTimeSlot.createCarReservationTimeSlot(startDateTime, days);

        int totalCarsOfType = carFleet.get(carType);
        long reservedCarsOfType = getNumberOfOverlappingReservations(carType, carReservationTimeSlot);

        return Math.max(0, totalCarsOfType - (int) reservedCarsOfType);
    }

    public List<CarReservation> getCarReservations() {
        return this.carReservations;
    }

    private boolean isReservationPossible(CarType carType, CarReservationTimeSlot carReservationTimeSlot) {
        int totalCarsOfType = carFleet.get(carType);
        long reservedCarsOfType = getNumberOfOverlappingReservations(carType, carReservationTimeSlot);

        return reservedCarsOfType < totalCarsOfType;
    }

    private long getNumberOfOverlappingReservations(CarType carType, CarReservationTimeSlot carReservationTimeSlot) {
        return carReservations.stream()
                .filter(carReservation -> carReservation.isCarReservationForGivenCarType(carType))
                .filter(carReservation -> carReservation.isCarReservationTimeSlotOverlappingWithAnotherTimeSlot(carReservationTimeSlot))
                .count();
    }
}
