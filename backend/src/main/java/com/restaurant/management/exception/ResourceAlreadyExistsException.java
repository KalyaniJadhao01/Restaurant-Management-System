package com.restaurant.management.exception;


public class ResourceAlreadyExistsException extends RuntimeException {


    public ResourceAlreadyExistsException(String message) {
        super(message);
    }

}