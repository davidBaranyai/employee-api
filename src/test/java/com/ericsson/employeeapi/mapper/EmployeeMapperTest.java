package com.ericsson.employeeapi.mapper;

import com.ericsson.employeeapi.dto.EmployeeDto;
import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.model.Employee;
import com.ericsson.employeeapi.model.Role;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeMapperTest {
    private final EmployeeMapper mapper = Mappers.getMapper(EmployeeMapper.class);

    @Test
    void toEmployeeDto_shouldMapAllFieldsCorrectly() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Alice");
        employee.setAge(30);
        employee.setTeam("Engineering");
        employee.setRole(Role.DEVELOPER);
        employee.setManagerId(10);
        employee.setStartDate(LocalDate.of(2020, 1, 1));

        EmployeeDto dto = mapper.toEmployeeDto(employee);

        assertNotNull(dto);
        assertEquals(employee.getId(), dto.getId());
        assertEquals(employee.getName(), dto.getName());
        assertEquals(employee.getAge(), dto.getAge());
        assertEquals(employee.getTeam(), dto.getTeam());
        assertEquals(employee.getRole(), dto.getRole());
        assertEquals(employee.getManagerId(), dto.getManagerId());
        assertEquals(employee.getStartDate(), dto.getStartDate());
    }

    @Test
    void updateEmployeeFromRequest_shouldUpdateAllFields() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Old Name");
        employee.setAge(30);
        employee.setTeam("Old Team");
        employee.setRole(Role.MANAGER);
        employee.setManagerId(null);
        employee.setStartDate(LocalDate.of(2020, 1, 1));

        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setName("New Name");
        requestDto.setAge(25);
        requestDto.setTeam("New Team");
        requestDto.setRole(Role.DEVELOPER);
        requestDto.setManagerId(100);
        requestDto.setStartDate(LocalDate.of(2023, 5, 10));

        mapper.updateEmployeeFromRequest(employee, requestDto);

        assertEquals("New Name", employee.getName());
        assertEquals(25, employee.getAge());
        assertEquals("New Team", employee.getTeam());
        assertEquals(Role.DEVELOPER, employee.getRole());
        assertEquals(100, employee.getManagerId());
        assertEquals(LocalDate.of(2023, 5, 10), employee.getStartDate());

        assertEquals(1, employee.getId());
    }

    @Test
    void toEmployeeFromRequest_shouldMapAllFieldsCorrectly() {
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setName("Alice Smith");
        requestDto.setAge(28);
        requestDto.setTeam("Engineering");
        requestDto.setRole(Role.DEVELOPER);
        requestDto.setManagerId(200);
        requestDto.setStartDate(LocalDate.of(2024, 3, 15));

        Employee employee = mapper.toEmployeeFromRequest(requestDto);

        assertNull(employee.getId());
        assertEquals("Alice Smith", employee.getName());
        assertEquals(28, employee.getAge());
        assertEquals("Engineering", employee.getTeam());
        assertEquals(Role.DEVELOPER, employee.getRole());
        assertEquals(200, employee.getManagerId());
        assertEquals(LocalDate.of(2024, 3, 15), employee.getStartDate());
    }


}