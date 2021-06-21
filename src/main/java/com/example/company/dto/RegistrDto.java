package com.example.company.dto;

import lombok.Data;

@Data
public class RegistrDto {
    private String firtsname;
    private String lastname;
    private String email;
    private String password;
    private Integer role_id;
    private Integer salary;
}
