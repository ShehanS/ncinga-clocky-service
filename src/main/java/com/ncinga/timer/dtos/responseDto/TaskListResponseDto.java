package com.ncinga.timer.dtos.responseDto;

import com.ncinga.timer.dtos.requestDto.ListInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskListResponseDto {
    private ArrayList<ResponseStatusDto> response_status;
    private ListInfoDto list_info;
    private ArrayList<TaskDto> tasks;
}
