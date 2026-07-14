package com.mb;

import java.util.Objects;
import java.util.UUID;

public record CarReservation(
        UUID id,
        CarType carType,
        CarReservationTimeSlot carReservationTimeSlot
) {
    public CarReservation {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(carType, "carType must not be null");
        Objects.requireNonNull(carReservationTimeSlot, "period must not be null");
    }

    public static CarReservation createCarReservation(CarType carType, CarReservationTimeSlot carReservationTimeSlot) {
        return new CarReservation(UUID.randomUUID(), carType, carReservationTimeSlot);
    }

    public boolean isCarReservationForGivenCarType(CarType carType) {
        return this.carType == carType;
    }

    public boolean isCarReservationTimeSlotOverlappingWithAnotherTimeSlot(CarReservationTimeSlot carReservationTimeSlot) {
        return this.carReservationTimeSlot.isOverlapping(carReservationTimeSlot);
    }
}
