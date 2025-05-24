package com.ericsson.employeeapi.repository;

import com.ericsson.employeeapi.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb;DATABASE_TO_LOWER=TRUE;CASE_INSENSITIVE_IDENTIFIERS=TRUE",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "spring.datasource.driverClassName=org.h2.Driver"
})
@Import(LiquibaseTestConfig.class)
@Sql(value = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class EmployeeRepositoryTest {

    @Autowired
    EmployeeRepository underTest;

    @Test
    void findByManagerId() {
        List<Employee> result = underTest.findByManagerId(1);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}