package com.example.company.service;

import com.example.company.dto.TaskDto;
import com.example.company.entity.Employee;
import com.example.company.entity.Task;
import com.example.company.entity.TaskStatus;
import com.example.company.repository.EmployeeRepository;
import com.example.company.repository.TaskRepository;
import com.example.company.repository.TaskStatusRepository;
import com.example.company.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskAttachService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthService authService;
    @Autowired
    TaskStatusRepository taskStatusRepository;


    // Vazifani ishchilarga biriktirish
    public ApiResponse taskAttach(TaskDto taskDto) {
        int size = taskDto.getVazifaoluvchi().size();
        List<Employee> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Optional<Employee> op = employeeRepository.findByEmail(taskDto.getVazifaoluvchi().get(i));
            if (!op.isPresent()) {
                size = 0;
            } else {
                list.add(op.get());
            }
        }
        if (size != 0) {
            Optional<Employee> op = employeeRepository.findByEmail(taskDto.getVazifaberuvchi());
            if (op.isPresent()) {
                TaskStatus taskStatus = new TaskStatus();
                taskStatusRepository.save(taskStatus);
                Task task = new Task(taskDto.getName(), taskDto.getComment(), list, op.get(), taskStatus, taskDto.getDeadline());
                taskRepository.save(task);
                for (String s : taskDto.getVazifaoluvchi()) {
                    authService.sendEmail(s, task.getId());
                }
                return new ApiResponse("Vazifalar jo'natildi", true);
            } else {
                return new ApiResponse("Bunday vazifa beruvchi topilmadi", false);
            }
        } else {
            return new ApiResponse("Vazifa oluvchi topilmadi", false);
        }
    }

    //Vazifaga javob berish
    public ApiResponse readyTask() {
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Task> optional = taskRepository.findByEmployeeContaining(employee);
        if (optional.isPresent()) {
            Task task = optional.get();
            boolean b = authService.sendEmail(employee.getEmail(), task.getId());
            if (b) {
                TaskStatus taskStatus = task.getTaskStatus();
                taskStatus.setCurrentTask(false);
                taskStatus.setFinishedTask(true);
                taskStatusRepository.save(taskStatus);
                task.setTaskStatus(taskStatus);
                taskRepository.save(task);
                return new ApiResponse("Vazifa jo'natildi",true);
            } else {
                return new ApiResponse("Vazifa jo'natishda xatolik,qaytadan urinib ko'ring", false);
            }
        } else {
            return new ApiResponse("Vazifa sizga tegishli emas", false);
        }

    }
}
