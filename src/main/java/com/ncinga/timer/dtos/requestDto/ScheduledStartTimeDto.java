package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledStartTimeDto {
    private String display_value;
    private String value;
}
