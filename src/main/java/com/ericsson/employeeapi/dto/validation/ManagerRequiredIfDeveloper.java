package com.ericsson.employeeapi.dto.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ManagerFieldValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ManagerRequiredIfDeveloper {
    String message() default "Manager ID is required when role is DEVELOPER";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}