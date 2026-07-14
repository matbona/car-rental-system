package com.mb;

public class CarTypeNotExistException extends RuntimeException {
    public CarTypeNotExistException(String message) {
        super(message);
    }
}
