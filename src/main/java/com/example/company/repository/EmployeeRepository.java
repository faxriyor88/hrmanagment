package com.example.company.repository;

import com.example.company.entity.Employee;
import com.example.company.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RepositoryRestResource(path = "employee")
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByEmailAndPassword(String email, String password);

    Optional<Employee> findByEmailAndEmailCode(String email, String emailCode);

    List<Employee> findByRole(Role role);

}
