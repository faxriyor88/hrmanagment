package com.example.company.dto;


import lombok.Data;


import java.sql.Timestamp;
import java.time.LocalDateTime;

import java.util.List;
@Data
public class TaskDto {
    private String name;
    private String comment;
    private List<String>  vazifaoluvchi;
    private String vazifaberuvchi;
    private Timestamp deadline;
}
