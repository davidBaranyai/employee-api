package com.ericsson.employeeapi.dto.validation;

import com.ericsson.employeeapi.dto.EmployeeRequestDto;
import com.ericsson.employeeapi.model.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ManagerFieldValidator implements ConstraintValidator<ManagerRequiredIfDeveloper, EmployeeRequestDto> {
    @Override
    public boolean isValid(EmployeeRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getRole() == Role.DEVELOPER && dto.getManagerId() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Manager ID must be provided if role is DEVELOPER")
                    .addPropertyNode("managerId")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
