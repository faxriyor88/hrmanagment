package com.example.company.config;

import com.example.company.entity.Employee;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Configuration
public class KimYozganiniBilish implements AuditorAware<UUID> {
    @Override
    public Optional getCurrentAuditor() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if (authentication!=null&&authentication.isAuthenticated()&&!authentication.getPrincipal().equals("anonymouse")){
            Employee employee=(Employee) authentication.getPrincipal();
            return Optional.of(employee.getUuid());
        }else {
            return Optional.empty();
        }
    }
}
