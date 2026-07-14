package com.mb;

import java.time.LocalDateTime;
import java.util.Objects;

public record CarReservationTimeSlot(
        LocalDateTime startingDateTime,
        LocalDateTime endingDateTime
) {
    public CarReservationTimeSlot {
        Objects.requireNonNull(startingDateTime, "start must not be null");
        Objects.requireNonNull(endingDateTime, "end must not be null");

        if (!endingDateTime.isAfter(startingDateTime)) {
            throw new IllegalArgumentException("Reservation end must be after reservation start");
        }
    }

    public static CarReservationTimeSlot createCarReservationTimeSlot(LocalDateTime startingDateTime, int days) {
        Objects.requireNonNull(startingDateTime, "start must not be null");

        if (days < 1) {
            throw new IllegalArgumentException("Reservation must be at least one day long");
        }

        return new CarReservationTimeSlot(startingDateTime, startingDateTime.plusDays(days));
    }

    public boolean isOverlapping(CarReservationTimeSlot carReservationTimeSlot) {
        Objects.requireNonNull(carReservationTimeSlot, "other period must not be null");

        return startingDateTime.isBefore(carReservationTimeSlot.endingDateTime)
                & endingDateTime.isAfter(carReservationTimeSlot.startingDateTime);
    }
}
