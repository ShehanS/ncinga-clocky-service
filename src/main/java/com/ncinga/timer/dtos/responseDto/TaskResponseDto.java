package com.ncinga.timer.dtos.responseDto;
import com.ncinga.timer.dtos.requestDto.TaskDTO;
import lombok.Data;
import java.util.List;

@Data
public class TaskResponseDto {
    private List<TaskDTO.ResponseStatus> responseStatus;
    private TaskDTO.ListInfo listInfo;
    private List<TaskDTO.Task> tasks;
}
