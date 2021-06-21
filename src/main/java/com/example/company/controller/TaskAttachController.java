package com.example.company.controller;

import com.example.company.dto.TaskDto;
import com.example.company.response.ApiResponse;
import com.example.company.service.ConfirmService;
import com.example.company.service.TaskAttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class TaskAttachController {
    @Autowired
    ConfirmService confirmService;
    @Autowired
    TaskAttachService taskAttachService;
    @GetMapping("/taskattachconfirm")
    public ResponseEntity<?> task(@RequestParam UUID uuid,String sendingEmail){
        ApiResponse apiResponse = confirmService.verifyEmailTask(sendingEmail, uuid);
        return ResponseEntity.status(apiResponse.isStatus()?201:409).body(apiResponse);
    }

    @PostMapping("/taskattach")
    public ResponseEntity<?> taskAttach(@RequestBody TaskDto taskDto){
        ApiResponse apiResponse = taskAttachService.taskAttach(taskDto);
        return ResponseEntity.status(apiResponse.isStatus()?201:409).body(apiResponse);
    }

    @PostMapping("/readytask")
    public ResponseEntity<?> readyTask(){
        ApiResponse apiResponse = taskAttachService.readyTask();
        return ResponseEntity.status(apiResponse.isStatus()?201:409).body(apiResponse);
    }


}
