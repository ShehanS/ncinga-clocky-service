package com.ncinga.timer.dtos;

import com.ncinga.timer.dtos.requestDto.AddEditWorklogDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskWorklogDto {
    private AddEditWorklogDto worklog;
}
