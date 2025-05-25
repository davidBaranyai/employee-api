package com.ericsson.employeeapi.exception;

public class EmployeeNotFoundException extends RuntimeException {
    private Integer id;

    public EmployeeNotFoundException(Integer id) {
        super();
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
