package com.example.company.service;


import com.example.company.dto.EmployeeInformationDto;
import com.example.company.dto.InformationDto;
import com.example.company.dto.RegistrDto;
import com.example.company.entity.*;

import com.example.company.entity.enums.Rolename;
import com.example.company.repository.*;
import com.example.company.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TurniketRepository turniketRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;

    // Employeeni ro'yxatdan o'tkazish
    public ApiResponse registr(RegistrDto registrDto) {
        Employee employee1 = (Employee) SecurityContextHolder.getContext().getAuthentication();
        if (employee1.getRole().equals(Rolename.ROLE_DIRECTOR)||employee1.getRole().equals(Rolename.ROLE_HR_MANAGER)){
            Optional<Employee> byEmail = employeeRepository.findByEmail(registrDto.getEmail());
        if (!byEmail.isPresent()) {
            Optional<Role> byId = roleRepository.getRole(registrDto.getRole_id());
            if (byId.isPresent()) {
                String emailCode = UUID.randomUUID().toString();
                Employee employee = new Employee(registrDto.getFirtsname(), registrDto.getLastname(),
                        Collections.singleton(byId.get()), registrDto.getSalary(),
                        emailCode, registrDto.getEmail(), passwordEncoder.encode(registrDto.getPassword()));
                employeeRepository.save(employee);
                boolean b = sendEmail(registrDto.getEmail(), emailCode);
                if (b) {
                    return new ApiResponse("Emailga kod jo'natildi", true);
                } else {
                    return new ApiResponse("Kod jo'natilmadi", false);
                }
            } else {
                return new ApiResponse("Bunday rol topilmadi", false);
            }
        } else {
            return new ApiResponse("Bunday email mavjud", false);
        }
    }else {
            return new ApiResponse("Sizda bu operatsiyani bajarishga huquq yo'q",false);
        }
    }

    //Xodim korxonaga kirishi
    public ApiResponse turniketIncome(String username) {
        Optional<Employee> optional = employeeRepository.findByEmail(username);
        if (optional.isPresent()) {
            Turniket turniket = new Turniket();
            turniket.setIncome(true);
            turniket.setEmployee(optional.get());
            turniketRepository.save(turniket);
            return new ApiResponse("Xodim korxonaga kirdi", true);
        } else {
            return new ApiResponse("Korxonada bunday xodim topilmadi", false);
        }
    }

    //Xodimni korxonadan chiqishi
    public ApiResponse turniketOutcome(String username) {
        Optional<Employee> optional = employeeRepository.findByEmail(username);
        if (optional.isPresent()) {
            Turniket turniket = new Turniket();
            turniket.setIncome(true);
            turniket.setEmployee(optional.get());
            turniketRepository.save(turniket);
            return new ApiResponse("Xodim korxonadan chiqdi", true);
        } else {
            return new ApiResponse("Korxonada bunday xodim topilmadi", false);
        }
    }

    // Emailga xabar jo'natish
    public boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Faxriyor");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Tasdqiqlash kodi:");
            simpleMailMessage.setText("<a href='http://localhost:8080/api/auth/verifyemail?emailCode=" + emailCode + "&sendingEmail=" + sendingEmail + "'>Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Employee> optional = employeeRepository.findByEmail(email);
        return optional.orElseThrow(() -> new UsernameNotFoundException("Topilmadi"));
    }

    public boolean sendEmail(String sendingEmail, UUID uuid) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("Faxriyor");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Tasdqiqlash kodi:");
            simpleMailMessage.setText("<a href='http://localhost:8080/api/auth/taskattachconfirm?uuid=" + uuid + "&sendingEmail=" + sendingEmail + "'>Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Xodimlar ro'yxatini olish
    public List<Employee> getEmployee() {
        Employee employee = (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (employee.getRole().equals("ROLE_DIRECTOR") || employee.getRole().equals("ROLE_HR_MANAGER")) {
            return employeeRepository.findByRole(roleRepository.findByRolename(Rolename.ROLE_STAFF));
        } else {
            return new ArrayList<>();
        }
    }

    //Xodimlarni kelib ketishi va bajargan vazifalari
    public InformationDto employeeInformation(EmployeeInformationDto informationDto) {
        Optional<Turniket> optional = turniketRepository.findByTimeAfterAndTimeBeforeAndEmployee_Email(informationDto.getTime(), informationDto.getTime2(), informationDto.getUsername());
        if (optional.isPresent()) {
            Optional<Task> optional1 = taskRepository.findByEmployeeContainingAndDeadlineAfterAndDeadlineBefore(employeeRepository.findByEmail(informationDto.getUsername()).get(), informationDto.getTime(), informationDto.getTime2());
            if (optional1.isPresent()) {
                return new InformationDto(optional1.get(), optional.get());
            } else {
                return new InformationDto();
            }
        } else {
            return new InformationDto();
        }
    }
    // Xodim bo'yicha oyliklarni ko'rish
    public SalaryHistory getSalaryByEmployee(String username){
        Optional<SalaryHistory> optional=salaryHistoryRepository.findByEmployee_Email(username);
        return optional.orElseThrow(() -> new UsernameNotFoundException("Bunday xodim topilmadi"));
    }
}