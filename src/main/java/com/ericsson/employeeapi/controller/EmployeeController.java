package com.ericsson.employeeapi.controller;

import com.ericsson.employeeapi.dto.EmployeeDto;
import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.dto.EmploymentDaysDto;
import com.ericsson.employeeapi.exception.EmployeeNotFoundException;
import com.ericsson.employeeapi.service.EmployeeService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@OpenAPIDefinition(
        info = @Info(
                version = "1.0.0-SNAPSHOT",
                title = "Employee OpenAPI Definition",
                description = "This is an API for managing employees")
)
@Tag(name = "Employee API", description = "Employee related APIs")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Operation(summary = "Get a list of all employee", parameters = @Parameter(name = "manager",
            description = "ID of a manager", example = "123"))
    @GetMapping("/employees")
    @ResponseBody
    public List<EmployeeDto> getEmployees(@RequestParam(required = false, name = "manager") Integer manager) {
        return (manager != null) ? employeeService.getEmployeesByManagerId(manager)
                : employeeService.getAllEmployees();
    }

    @Operation(summary = "Get the number of days the employee has in employment")
    @GetMapping("/employee/{id}/days")
    @ResponseBody
    public EmploymentDaysDto getEmploymentDays(@PathVariable(name = "id") Integer id) {
        return employeeService.getEmploymentDays(id);
    }

    @Operation(summary = "Create new employee")
    @PostMapping("/employee")
    @ResponseBody
    public EmployeeDto createEmployee(@Valid @RequestBody EmployeeRequestDto request) {
        return employeeService.createEmployee(request);
    }

    @Operation(summary = "Update existing employee")
    @PutMapping("/employee/{id}")
    @ResponseBody
    public EmployeeDto putEmployee(@PathVariable(name = "id") Integer id, @Valid @RequestBody EmployeeRequestDto request) {
        return employeeService.updateEmployee(id, request);
    }

    @Operation(summary = "Delete existing employee")
    @DeleteMapping("/employee/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable(name = "id") Integer id) {
        employeeService.deleteEmployee(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmployeeNotFoundException.class)
    public Map<String, String> handleValidationExceptions(EmployeeNotFoundException ex) {
        return Map.of("id", ex.getId().toString());
    }
}
