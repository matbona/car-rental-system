package com.mb;

public record CarReservation(
        CarType carType,
        CarReservationTimeSlot carReservationTimeSlot
) {
    public boolean isCarReservationForGivenCarType(CarType carType) {
        return this.carType == carType;
    }

    public boolean isCarReservationTimeSlotOverlappingWithAnotherTimeSlot(CarReservationTimeSlot carReservationTimeSlot) {
        return false;
    }
}
