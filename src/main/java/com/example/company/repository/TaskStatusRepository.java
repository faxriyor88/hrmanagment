package com.example.company.repository;

import com.example.company.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "taskstatus")
public interface TaskStatusRepository extends JpaRepository<TaskStatus,Integer> {
}
