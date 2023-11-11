package com.ncinga.timer.dtos.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProjectsDto {
    public ArrayList<ProjectDto> projects;
    public ArrayList<ResponseStatusDto> response_status;
    public ListInfoDto list_info;
}
