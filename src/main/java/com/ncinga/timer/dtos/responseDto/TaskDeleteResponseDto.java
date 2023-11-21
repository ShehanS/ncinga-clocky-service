package com.ncinga.timer.dtos.responseDto;

import com.ncinga.timer.dtos.requestDto.DeleteTaskDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDeleteResponseDto {
    private DeleteTaskDto response_status;
}
