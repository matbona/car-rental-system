package com.mb;

import java.util.EnumMap;

public class CarRentalSystemService {

    private final EnumMap<CarType, Integer> carFleet;

    public CarRentalSystemService(EnumMap<CarType, Integer> carFleet) {
        this.carFleet = carFleet;
    }

}
