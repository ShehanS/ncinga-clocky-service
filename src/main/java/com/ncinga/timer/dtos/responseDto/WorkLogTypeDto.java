package com.ncinga.timer.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkLogTypeDto {
    private String name;
    private boolean inactive;
    private String id;
}
