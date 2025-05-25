package com.ericsson.employeeapi.exception;

public class IncorrectManagerIdException extends RuntimeException {
    public IncorrectManagerIdException(Integer id) {
        super(String.format("ID does not belong to a manager: '%d'", id));
    }
}
