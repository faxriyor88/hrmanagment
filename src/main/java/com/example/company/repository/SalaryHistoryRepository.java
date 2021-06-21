package com.example.company.repository;

import com.example.company.entity.SalaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


import java.util.Optional;

@RepositoryRestResource
public interface SalaryHistoryRepository extends JpaRepository<SalaryHistory,Integer> {
    Optional<SalaryHistory> findByEmployee_Email(String employee_email);
}
