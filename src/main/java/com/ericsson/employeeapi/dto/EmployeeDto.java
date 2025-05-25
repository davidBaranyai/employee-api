package com.ericsson.employeeapi.dto;

import com.ericsson.employeeapi.model.Role;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmployeeDto {
    private Integer id;
    private String name;
    private Integer age;
    private String team;
    private Role role;
    private Integer managerId;
    private LocalDate startDate;
}
