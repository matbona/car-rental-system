package com.mb;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class CarReservationTimeSlotTest {

    @Test
    void shouldDetectOverlappingPeriods() {
        // Given
        // When
        CarReservationTimeSlot existing = new CarReservationTimeSlot(
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 3, 10, 0)
        );

        CarReservationTimeSlot requested = new CarReservationTimeSlot(
                LocalDateTime.of(2026, 8, 2, 10, 0),
                LocalDateTime.of(2026, 8, 4, 10, 0)
        );

        // Then
        assertThat(existing.isOverlapping(requested)).isTrue();
        assertThat(requested.isOverlapping(existing)).isTrue();
    }

    @Test
    void shouldNotOverlapWhenExistingReservationEndsExactlyAtRequestedStart() {
        CarReservationTimeSlot existing = new CarReservationTimeSlot(
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 3, 10, 0)
        );

        CarReservationTimeSlot requested = new CarReservationTimeSlot(
                LocalDateTime.of(2026, 8, 3, 10, 0),
                LocalDateTime.of(2026, 8, 4, 10, 0)
        );

        assertThat(existing.isOverlapping(requested)).isFalse();
        assertThat(requested.isOverlapping(existing)).isFalse();
    }
}