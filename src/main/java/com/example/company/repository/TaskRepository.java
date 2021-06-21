package com.example.company.repository;

import com.example.company.entity.Employee;
import com.example.company.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RepositoryRestResource(path = "task")
public interface TaskRepository extends JpaRepository<Task, UUID> {
    boolean existsByEmployeeContaining(Employee employee);
    Optional<Task> findByEmployeeContaining(Employee employee);

    Optional<Task> findByEmployeeContainingAndDeadlineAfterAndDeadlineBefore(Employee employee, Timestamp deadline, Timestamp deadline2);


}
