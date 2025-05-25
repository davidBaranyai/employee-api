package com.ericsson.employeeapi.dto.validation;

import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.model.Role;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagerFieldValidatorTest {

    private ManagerFieldValidator underTest = new ManagerFieldValidator();
    @Mock
    private ConstraintValidatorContext context;
    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder violationBuilder;

    @Test
    void whenRoleIsDeveloperAndManagerIdIsNull_thenValidationFails() {
        // Given
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setName("Dev");
        dto.setAge(25);
        dto.setTeam("Engineering");
        dto.setRole(Role.DEVELOPER);
        dto.setManagerId(null);
        dto.setStartDate(LocalDate.now());

        // Mock violation building behavior
        when(context.buildConstraintViolationWithTemplate(anyString()))
                .thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("managerId"))
                .thenReturn(mock(ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext.class));

        // When
        boolean result = underTest.isValid(dto, context);

        // Then
        assertFalse(result);

        // Verify that default violation was disabled and custom one was added
        InOrder inOrder = inOrder(context, violationBuilder);
        inOrder.verify(context).disableDefaultConstraintViolation();
        inOrder.verify(context).buildConstraintViolationWithTemplate("Manager ID must be provided if role is DEVELOPER");
        inOrder.verify(violationBuilder).addPropertyNode("managerId");
    }

    @Test
    void whenRoleIsDeveloperAndManagerIdIsPresent_thenValidationPasses() {
        // Given
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setName("Dev");
        dto.setAge(25);
        dto.setTeam("Engineering");
        dto.setRole(Role.DEVELOPER);
        dto.setManagerId(123);
        dto.setStartDate(LocalDate.now());

        // When
        boolean result = underTest.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context); // No violation interactions needed
    }

    @Test
    void whenRoleIsManager_thenValidationPassesRegardlessOfManagerId() {
        // Given
        EmployeeRequestDto dto = new EmployeeRequestDto();
        dto.setName("Manager");
        dto.setAge(40);
        dto.setTeam("Management");
        dto.setRole(Role.MANAGER);
        dto.setManagerId(null); // This is fine
        dto.setStartDate(LocalDate.now());

        // When
        boolean result = underTest.isValid(dto, context);

        // Then
        assertTrue(result);
        verifyNoInteractions(context);
    }

}