package com.mb;

import java.util.UUID;

public record CarReservation(
        UUID id,
        CarType carType,
        CarReservationTimeSlot carReservationTimeSlot
) {
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
