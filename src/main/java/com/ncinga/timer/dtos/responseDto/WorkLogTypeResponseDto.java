package com.ncinga.timer.dtos.responseDto;

import com.ncinga.timer.dtos.requestDto.ListInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkLogTypeResponseDto {
    private List<WorkLogTypeDto> worklog_type;
    private ListInfoDto list_info;
}
