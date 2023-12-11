package com.ncinga.timer.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSpentDto {
    private String hours;
    private String minutes;
    private String value;
}
