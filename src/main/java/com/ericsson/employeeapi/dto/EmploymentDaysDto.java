package com.ericsson.employeeapi.dto;

import lombok.Data;

@Data
public class EmploymentDaysDto {
    private Long employmentDays;

    public EmploymentDaysDto(Long employmentDays) {
        this.employmentDays = employmentDays;
    }
}
