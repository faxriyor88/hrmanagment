package com.example.company.service;

import com.example.company.entity.Employee;
import com.example.company.entity.Task;
import com.example.company.entity.TaskStatus;
import com.example.company.repository.EmployeeRepository;
import com.example.company.repository.TaskRepository;
import com.example.company.repository.TaskStatusRepository;
import com.example.company.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ConfirmService {
    @Autowired
    EmployeeRepository employeeRepository;
@Autowired
    TaskRepository taskRepository;
@Autowired
    TaskStatusRepository taskStatusRepository;

    // Emailni tasdiqlash
    public ApiResponse verifyEmail(String emailCode, String sendingEmail) {
        Optional<Employee> optional = employeeRepository.findByEmailAndEmailCode(sendingEmail, emailCode);
        if (optional.isPresent()) {
            Employee employee = optional.get();
            employee.setEnabled(true);
            employee.setEmailCode(null);
            employeeRepository.save(employee);
            return new ApiResponse("Email Tasdiqlandi",true);
        }else {
            return new ApiResponse("Tasdiqlanmadi",false);
        }
    }
    //Vazifani qabul qilish
    public ApiResponse verifyEmailTask(String sendingEmail, UUID uuid){
        Optional<Task> optional = taskRepository.findById(uuid);
        if (optional.isPresent()){
            Task task= optional.get();
            boolean contains = task.getEmployee().contains(employeeRepository.findByEmail(sendingEmail).get());
            if (contains){
                TaskStatus taskStatus = task.getTaskStatus();
                taskStatus.setCurrentTask(true);
                taskStatus.setNewTas(false);
                taskStatusRepository.save(taskStatus);
                task.setTaskStatus(taskStatus);
                taskRepository.save(task);
                return new ApiResponse("Vazifa olindi",true);
            }else {
                return new ApiResponse("Bunday vazifa oluvchi topilmadi",false);
            }
        }else {
            return new ApiResponse("Bunday vazifa topilmadi",false);
        }
    }
}
