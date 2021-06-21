package com.example.company.dto;

import com.example.company.entity.Task;
import com.example.company.entity.Turniket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InformationDto {
    private Task task;
    private Turniket turniket;

}
