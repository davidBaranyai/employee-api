package com.ericsson.employeeapi.controller;

import com.ericsson.employeeapi.dto.EmployeeDto;
import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.dto.EmploymentDaysDto;
import com.ericsson.employeeapi.exception.EmployeeNotFoundException;
import com.ericsson.employeeapi.model.Role;
import com.ericsson.employeeapi.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getEmployees_shouldReturnListOfEmployees() throws Exception {
        EmployeeDto dto1 = new EmployeeDto();
        dto1.setId(1);
        dto1.setName("Alice");
        EmployeeDto dto2 = new EmployeeDto();
        dto2.setId(2);
        dto2.setName("Bob");

        when(employeeService.getAllEmployees()).thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob"));
    }

    @Test
    void getEmployeesByManagerId_shouldReturnFilteredList() throws Exception {
        EmployeeDto dto = new EmployeeDto();
        dto.setId(1);
        dto.setName("Alice");

        when(employeeService.getEmployeesByManagerId(100)).thenReturn(List.of(dto));

        mockMvc.perform(get("/employees").param("manager", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void getEmploymentDays_shouldReturnCorrectDays() throws Exception {
        EmploymentDaysDto dto = new EmploymentDaysDto(15L);

        when(employeeService.getEmploymentDays(1)).thenReturn(dto);

        mockMvc.perform(get("/employee/1/days"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employmentDays").value(15));
    }

    @Test
    void createEmployee_shouldReturnCreatedEmployee() throws Exception {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setName("Alice");
        request.setAge(30);
        request.setTeam("Engineering");
        request.setRole(Role.DEVELOPER);
        request.setManagerId(101);
        request.setStartDate(LocalDate.now());

        EmployeeDto responseDto = new EmployeeDto();
        responseDto.setId(1);
        responseDto.setName("Alice");

        when(employeeService.createEmployee(request)).thenReturn(responseDto);

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void putEmployee_shouldReturnUpdatedEmployee() throws Exception {
        EmployeeRequestDto request = new EmployeeRequestDto();
        request.setName("Bob Updated");
        request.setAge(35);
        request.setTeam("Support");
        request.setRole(Role.MANAGER);
        request.setStartDate(LocalDate.now());

        EmployeeDto updated = new EmployeeDto();
        updated.setId(2);
        updated.setName("Bob Updated");

        when(employeeService.updateEmployee(eq(2), any())).thenReturn(updated);

        mockMvc.perform(put("/employee/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Bob Updated"));
    }

    @Test
    void deleteEmployee_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/employee/5"))
                .andExpect(status().isNoContent());

        Mockito.verify(employeeService).deleteEmployee(5);
    }

    @Test
    void whenPostInvalidRequest_thenReturnsValidationErrors() throws Exception {
        String invalidJson = """
                    {
                        "name": "",
                        "age": null,
                        "team": "",
                        "role": null,
                        "managerId": null,
                        "startDate": null
                    }
                """;

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Name is required"))
                .andExpect(jsonPath("$.age").value("Age is required"))
                .andExpect(jsonPath("$.team").value("Team is required"))
                .andExpect(jsonPath("$.role").value("Role is required"))
                .andExpect(jsonPath("$.startDate").value("Start date is required"));
    }

    @Test
    void putEmployee_shouldReturn404_whenEmployeeNotFound() throws Exception {
        Integer employeeId = 1;
        EmployeeRequestDto requestDto = new EmployeeRequestDto();
        requestDto.setName("John");
        requestDto.setAge(30);
        requestDto.setTeam("Dev");
        requestDto.setRole(Role.MANAGER);
        requestDto.setStartDate(LocalDate.now());

        // Mock service to throw EmployeeNotFoundException
        when(employeeService.updateEmployee(eq(employeeId), any(EmployeeRequestDto.class)))
                .thenThrow(new EmployeeNotFoundException(employeeId));

        // Convert request DTO to JSON string
        String requestJson = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.id").value("1"));
    }
}