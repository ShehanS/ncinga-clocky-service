package com.ncinga.timer.interfacrs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncinga.timer.dtos.requestDto.*;
import com.ncinga.timer.dtos.responseDto.OwnerResponseDto;
import com.ncinga.timer.dtos.responseDto.TaskDto;
import com.ncinga.timer.exceptions.RefreshTokenHasExpired;

import java.util.List;

public interface IManageEngine {
    List<ProjectDto> getProjectList(String refreshToken, String email) throws RefreshTokenHasExpired;

    ProjectTemplateDto getProjectById(String refreshToken, String projectId) throws RefreshTokenHasExpired;

    OwnerResponseDto getProjectOwners(String refreshToken, OwnerListRequestDto ownerListRequestDto) throws RefreshTokenHasExpired;

    List<TaskDto> getTaskByEmail(String refreshToken, String email) throws RefreshTokenHasExpired;

    List<TaskDto> getTaskByProjectId(String refreshToken, String projectId) throws RefreshTokenHasExpired;

    List<OwnerDto> getOwners(String refreshToken, String projectId) throws RefreshTokenHasExpired;

    List<PriorityDto> getProjectPriority(String refreshToken, String projectId) throws RefreshTokenHasExpired;

    List<StatusDto> getProjectStatus(String refreshToken, String projectId) throws RefreshTokenHasExpired;

    ProjectTask addTask(String refreshToken, String projectId, AddEditTaskDto task) throws RefreshTokenHasExpired, JsonProcessingException;
    ProjectTask updateTask(String refreshToken, String projectId, String taskId, AddEditTaskDto task) throws RefreshTokenHasExpired, JsonProcessingException;
    ProjectTask getTask(String refreshToken, String projectId, String taskId) throws RefreshTokenHasExpired, JsonProcessingException;
    DeleteTaskDto deleteTask(String refreshToken, String projectId, String taskId) throws RefreshTokenHasExpired, JsonProcessingException;
}
