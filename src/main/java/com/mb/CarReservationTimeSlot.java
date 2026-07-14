package com.mb;

import java.time.LocalDateTime;

public record CarReservationTimeSlot(
        LocalDateTime startingDateTime,
        LocalDateTime endingDateTime
) {
    public static CarReservationTimeSlot createCarReservationTimeSlot(LocalDateTime startingDateTime, int days) {
        return new CarReservationTimeSlot(startingDateTime, startingDateTime.plusDays(days));
    }
}
