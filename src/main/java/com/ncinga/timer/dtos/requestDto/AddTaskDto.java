package com.ncinga.timer.dtos.requestDto;

import com.ncinga.timer.dtos.responseDto.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddTaskDto {
    private NewTaskDto task;
}
