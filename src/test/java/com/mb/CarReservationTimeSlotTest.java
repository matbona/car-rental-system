package com.mb;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

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
        // Given
        // When
        CarReservationTimeSlot existing = new CarReservationTimeSlot(
                LocalDateTime.of(2026, 8, 1, 10, 0),
                LocalDateTime.of(2026, 8, 3, 10, 0)
        );

        CarReservationTimeSlot requested = new CarReservationTimeSlot(
                LocalDateTime.of(2026, 8, 3, 10, 0),
                LocalDateTime.of(2026, 8, 4, 10, 0)
        );

        // Then
        assertThat(existing.isOverlapping(requested)).isFalse();
        assertThat(requested.isOverlapping(existing)).isFalse();
    }

    @Test
    void shouldRejectZeroDays() {
        // Given
        // When
        LocalDateTime start = LocalDateTime.of(2026, 8, 1, 10, 0);

        // Then
        assertThatThrownBy(() -> CarReservationTimeSlot.createCarReservationTimeSlot(start, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least one day");
    }

    @Test
    void shouldRejectEndBeforeStart() {
        // Given
        // When
        LocalDateTime start = LocalDateTime.of(2026, 8, 3, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 8, 1, 10, 0);

        // Then
        assertThatThrownBy(() -> new CarReservationTimeSlot(start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("end must be after");
    }
}