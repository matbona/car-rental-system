package com.mb;

import java.time.LocalDateTime;
import java.util.EnumMap;

public class CarRentalSystemService {

    private final EnumMap<CarType, Integer> carFleet;

    public CarRentalSystemService(EnumMap<CarType, Integer> carFleet) {
        this.carFleet = carFleet;
    }

    public CarReservation reserveCar(CarType carType, LocalDateTime startDateTime, int days) {
        if (isReservationPossible(carType)) {
            return null;
        }

        return null;
    }

    public int getNumberOfAvailableCars(CarType carType, LocalDateTime startDateTime, int days) {
        return carFleet.get(carType);
    }

    private boolean isReservationPossible(CarType carType) {
        return true;
    }
}
