package com.example.company.controller;

import com.example.company.dto.EmployeeInformationDto;
import com.example.company.dto.RegistrDto;

import com.example.company.dto.TurniketDto;
import com.example.company.entity.Employee;
import com.example.company.entity.SalaryHistory;
import com.example.company.response.ApiResponse;
import com.example.company.service.AuthService;
import com.example.company.service.ConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    ConfirmService confirmService;
    @PostMapping("/registr")
    public ResponseEntity<?> registrEmployee(@RequestBody RegistrDto registrDto){
        ApiResponse registr = authService.registr(registrDto);
        return ResponseEntity.status(registr.isStatus()?201:409).body(registr);
    }

    @GetMapping("/auth/verifyemail")
    public ResponseEntity<?> verifyEmail(@RequestParam String emailCode,String sendingEmail){
        ApiResponse apiResponse = confirmService.verifyEmail(emailCode, sendingEmail);
        return ResponseEntity.status(apiResponse.isStatus()?201:409).body(apiResponse);
    }

    @PostMapping("/turniketincome")
    public ResponseEntity<?> income(@RequestBody TurniketDto turniketDto){
        ApiResponse apiResponse = authService.turniketIncome(turniketDto.getUsername());
        return ResponseEntity.status(apiResponse.isStatus()?201:409).body(apiResponse);
    }
    @PostMapping("/turniketoutcome")
    public ResponseEntity<?> outcome(@RequestBody TurniketDto turniketDto){
        ApiResponse apiResponse = authService.turniketIncome(turniketDto.getUsername());
        return ResponseEntity.status(apiResponse.isStatus()?201:409).body(apiResponse);
    }
    @GetMapping("/employeelist")
    public List<Employee> getEmployeeList(){
        List<Employee> employee = authService.getEmployee();
        return employee;
    }
    @GetMapping("/employeeinformation")
    public ResponseEntity<?> employeInformation(@RequestBody EmployeeInformationDto employeeInformationDto){
        return ResponseEntity.ok(authService.employeeInformation(employeeInformationDto));
    }

    @GetMapping("/salarybyemployee")
    public ResponseEntity<?> salary(@RequestParam  String username){
        SalaryHistory salaryByEmployee = authService.getSalaryByEmployee(username);
        return ResponseEntity.ok(salaryByEmployee);
    }

}
