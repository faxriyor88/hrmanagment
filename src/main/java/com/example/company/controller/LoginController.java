package com.example.company.controller;

import com.example.company.dto.LoginDto;
import com.example.company.jwt.JwtProwider;
import com.example.company.response.ApiResponse;
import com.example.company.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginController {
    @Autowired
    AuthService authService;
    @Autowired
    JwtProwider jwtProwider;
    @Autowired
    AuthenticationManager authenticationManager;
    @PostMapping("/loginemployee")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            String token = jwtProwider.generateToken(loginDto.getEmail());
            return ResponseEntity.status(201).body(new ApiResponse(token, true));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(403).body(e);
        }
    }
}
