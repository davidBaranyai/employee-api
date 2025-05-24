package com.ericsson.employeeapi.repository;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class LiquibaseTestConfig {
    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        System.out.println("Working dir: " + System.getProperty("user.dir"));
        liquibase.setChangeLog("file:./db/changelog-employees.xml");
        liquibase.setShouldRun(true);
        return liquibase;
    }
}
