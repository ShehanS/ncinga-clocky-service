package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriorityRequestDto {
    private ListInfoDto list_info;
    private List<PriorityDto> priority;
}
