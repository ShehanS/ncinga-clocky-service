package com.ncinga.timer.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogTypeDto {
    private String id;
    private boolean inactive;
    private String name;

}
