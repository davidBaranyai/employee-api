package com.ericsson.employeeapi.dto;

import com.ericsson.employeeapi.dto.validation.ManagerRequiredIfDeveloper;
import com.ericsson.employeeapi.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@ManagerRequiredIfDeveloper
public class EmployeeRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull(message = "Age is required")
    private Integer age;
    @NotBlank(message = "Team is required")
    private String team;
    @NotNull(message = "Role is required")
    private Role role;
    private Integer managerId;
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
}
