package com.example.company.repository;

import com.example.company.entity.Turniket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.sql.Timestamp;

import java.util.Optional;

@RepositoryRestResource(path = "turniket")
public interface TurniketRepository extends JpaRepository<Turniket,Integer> {
    Optional<Turniket> findByTimeAfterAndTimeBeforeAndEmployee_Email(Timestamp time, Timestamp time2, String employee_email);
}
