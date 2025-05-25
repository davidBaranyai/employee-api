package com.ericsson.employeeapi.mapper;

import com.ericsson.employeeapi.dto.EmployeeDto;
import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    EmployeeDto toEmployeeDto(Employee employee);

    Employee updateEmployeeFromRequest(@MappingTarget Employee employee, EmployeeRequestDto requestBodyDto);

    Employee toEmployeeFromRequest(EmployeeRequestDto requestBodyDto);
}
