package com.ncinga.timer.dtos.responseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogResponseDto {
    private List<WorkLogDto> worklogs;
}
