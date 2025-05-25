package com.ericsson.employeeapi.service;

import com.ericsson.employeeapi.dto.EmployeeDto;
import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.dto.EmploymentDaysDto;
import com.ericsson.employeeapi.exception.EmployeeNotFoundException;
import com.ericsson.employeeapi.mapper.EmployeeMapper;
import com.ericsson.employeeapi.model.Employee;
import com.ericsson.employeeapi.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private EmployeeMapper mapper;

    @InjectMocks
    private EmployeeService underTest;

    @Test
    void getAllEmployees_shouldReturnMappedDtos() {
        List<Employee> employees = List.of(new Employee(), new Employee());
        List<EmployeeDto> dtos = List.of(new EmployeeDto(), new EmployeeDto());

        when(repository.findAll()).thenReturn(employees);
        when(mapper.toEmployeeDto(any(Employee.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<EmployeeDto> result = underTest.getAllEmployees();

        assertEquals(2, result.size());
        verify(repository).findAll();
        verify(mapper, times(2)).toEmployeeDto(any());
    }

    @Test
    void getEmployeesByManagerId_shouldReturnMappedDtos() {
        Integer managerId = 123;
        List<Employee> employees = List.of(new Employee());
        List<EmployeeDto> dtos = List.of(new EmployeeDto());

        when(repository.findByManagerId(managerId)).thenReturn(employees);
        when(mapper.toEmployeeDto(any(Employee.class))).thenReturn(dtos.get(0));

        List<EmployeeDto> result = underTest.getEmployeesByManagerId(managerId);

        assertEquals(1, result.size());
        verify(repository).findByManagerId(managerId);
        verify(mapper).toEmployeeDto(any());
    }

    @Test
    void getEmploymentDays_shouldReturnDaysSinceStartDate() {
        Employee employee = new Employee();
        employee.setStartDate(LocalDate.now().minusDays(10));
        when(repository.findById(1)).thenReturn(Optional.of(employee));

        EmploymentDaysDto result = underTest.getEmploymentDays(1);

        assertEquals(10, result.getEmploymentDays());
    }

    @Test
    void createEmployee_shouldMapSaveAndReturnDto() {
        EmployeeRequestDto request = new EmployeeRequestDto();
        Employee entity = new Employee();
        Employee saved = new Employee();
        EmployeeDto dto = new EmployeeDto();

        when(mapper.toEmployeeFromRequest(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toEmployeeDto(saved)).thenReturn(dto);

        EmployeeDto result = underTest.createEmployee(request);

        assertEquals(dto, result);
        verify(mapper).toEmployeeFromRequest(request);
        verify(repository).save(entity);
        verify(mapper).toEmployeeDto(saved);
    }

    @Test
    void updateEmployee_shouldFindUpdateSaveAndReturnDto() {
        Integer id = 42;
        Employee existing = new Employee();
        Employee updated = new Employee();
        EmployeeRequestDto request = new EmployeeRequestDto();
        EmployeeDto dto = new EmployeeDto();

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(mapper.updateEmployeeFromRequest(existing, request)).thenReturn(updated);
        when(repository.save(updated)).thenReturn(updated);
        when(mapper.toEmployeeDto(updated)).thenReturn(dto);

        EmployeeDto result = underTest.updateEmployee(id, request);

        assertEquals(dto, result);
        verify(repository).findById(id);
        verify(mapper).updateEmployeeFromRequest(existing, request);
        verify(repository).save(updated);
        verify(mapper).toEmployeeDto(updated);
    }

    @Test
    void deleteEmployee_shouldCallRepositoryDelete() {
        Integer id = 99;

        underTest.deleteEmployee(id);

        verify(repository).deleteById(id);
    }

    @Test
    void updateEmployee_shouldThrowException_whenEmployeeNotFound() {
        // Given
        Integer employeeId = 123;
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        // Setup repository to return empty (no employee found)
        when(repository.findById(employeeId)).thenReturn(Optional.empty());

        // When & Then
        EmployeeNotFoundException thrown = assertThrows(EmployeeNotFoundException.class, () -> {
            underTest.updateEmployee(employeeId, requestDto);
        });

        assertEquals(employeeId, thrown.getId());  // assuming exception has getEmployeeId()
        verify(repository, never()).save(any());  // Save should never be called
    }

    @Test
    void getEmploymentDays_shouldReturnEmploymentDaysDto_whenEmployeeExists() {
        // Given
        Integer employeeId = 1;
        LocalDate startDate = LocalDate.now().minusDays(10); // started 10 days ago

        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setStartDate(startDate);

        when(repository.findById(employeeId)).thenReturn(Optional.of(employee));

        // When
        EmploymentDaysDto result = underTest.getEmploymentDays(employeeId);

        // Then
        assertNotNull(result);
        assertEquals(10, result.getEmploymentDays());

        verify(repository).findById(employeeId);
    }
}