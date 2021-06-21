package com.example.company.entity;

import com.example.company.entity.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Task {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String comment;
    @ManyToMany
    private List<Employee> employee;
    @ManyToOne
    private Employee vazifaberuvchi;
    @OneToOne
    private TaskStatus taskStatus;
    private Timestamp deadline;

    public Task(String name, String comment, List<Employee> employee, Employee vazifaberuvchi, TaskStatus taskStatus, Timestamp deadline) {
        this.name = name;
        this.comment = comment;
        this.employee = employee;
        this.vazifaberuvchi = vazifaberuvchi;
        this.taskStatus = taskStatus;
        this.deadline = deadline;
    }
}
