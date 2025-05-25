package com.ericsson.employeeapi.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.ericsson.employeeapi.repository")
public class RepositoryConfiguration {
}
