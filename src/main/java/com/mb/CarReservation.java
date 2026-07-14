package com.mb;

public record CarReservation(
        CarType carType,
        CarReservationTimeSlot carReservationTimeSlot
) {
    public static CarReservation createCarReservation(CarType carType, CarReservationTimeSlot carReservationTimeSlot) {
        return new CarReservation(carType, carReservationTimeSlot);
    }

    public boolean isCarReservationForGivenCarType(CarType carType) {
        return this.carType == carType;
    }

    public boolean isCarReservationTimeSlotOverlappingWithAnotherTimeSlot(CarReservationTimeSlot carReservationTimeSlot) {
        return this.carReservationTimeSlot.isOverlapping(carReservationTimeSlot);
    }
}
