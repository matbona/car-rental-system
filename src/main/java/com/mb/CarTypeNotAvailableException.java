package com.mb;

public class CarTypeNotAvailableException extends RuntimeException {
    public CarTypeNotAvailableException(String message) {
        super(message);
    }
}
