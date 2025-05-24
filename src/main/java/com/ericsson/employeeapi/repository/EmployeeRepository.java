package com.ericsson.employeeapi.repository;

import com.ericsson.employeeapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findByManagerId(Integer managerId);
}
