package com.ericsson.employeeapi.service;

import com.ericsson.employeeapi.dto.EmployeeDto;
import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.dto.EmploymentDaysDto;
import com.ericsson.employeeapi.exception.EmployeeNotFoundException;
import com.ericsson.employeeapi.mapper.EmployeeMapper;
import com.ericsson.employeeapi.model.Employee;
import com.ericsson.employeeapi.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = repository.findAll();

        return employees.stream().map(mapper::toEmployeeDto).toList();
    }

    public List<EmployeeDto> getEmployeesByManagerId(Integer managerId) {
        List<Employee> employees = repository.findByManagerId(managerId);

        return employees.stream().map(mapper::toEmployeeDto).toList();
    }

    public EmploymentDaysDto getEmploymentDays(Integer id) {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        long employmentDays = ChronoUnit.DAYS.between(employee.getStartDate(), LocalDate.now());

        return new EmploymentDaysDto(employmentDays);
    }

    public EmployeeDto createEmployee(EmployeeRequestDto request) {
        Employee employee = repository.save(mapper.toEmployeeFromRequest(request));

        return mapper.toEmployeeDto(employee);
    }

    public EmployeeDto updateEmployee(Integer id, EmployeeRequestDto request) {
        Employee employee = repository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
        employee = mapper.updateEmployeeFromRequest(employee, request);
        repository.save(employee);

        return mapper.toEmployeeDto(employee);
    }

    public void deleteEmployee(Integer id) {
        repository.deleteById(id);
    }
}
