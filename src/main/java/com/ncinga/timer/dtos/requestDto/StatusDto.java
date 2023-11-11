package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StatusDto {
    private boolean in_progress;
    private String internal_name;
    private String color;
    private String name;
    private String id;
}
