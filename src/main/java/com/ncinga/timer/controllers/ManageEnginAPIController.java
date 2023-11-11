package com.ncinga.timer.controllers;

import com.ncinga.timer.dtos.requestDto.*;
import com.ncinga.timer.dtos.responseDto.OwnerResponseDto;
import com.ncinga.timer.dtos.responseDto.ResponseDto;
import com.ncinga.timer.dtos.responseDto.TaskDto;
import com.ncinga.timer.exceptions.RefreshTokenHasExpired;
import com.ncinga.timer.service.ManageEngineAPIService;
import com.ncinga.timer.utilities.ResponseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/engine")
public class ManageEnginAPIController {

    @Autowired
    private ManageEngineAPIService manageEngineAPIService;

    @PostMapping(path = "/get-projects")
    public ResponseEntity<ResponseDto> getProjects(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }

        try {
            List<ProjectDto> projectDtoList = manageEngineAPIService.getProjectList(refreshToken, generalRequestDto.getEmail());
            ResponseDto responseDto = new ResponseDto(null, projectDtoList, null, ResponseCode.GET_PROJECT_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_ERROR);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-project-owners")
    public ResponseEntity<ResponseDto> getProjectsOwners(@RequestBody OwnerListRequestDto ownerListRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            OwnerResponseDto owners = manageEngineAPIService.getProjectOwners(refreshToken, ownerListRequestDto);
            ResponseDto responseDto = new ResponseDto(null, owners, null, ResponseCode.GET_PROJECT_OWNER_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_OWNER_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-task-by-mail")
    public ResponseEntity<ResponseDto> getTaskByEmail(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            List<TaskDto> taskList = manageEngineAPIService.getTaskByEmail(refreshToken, generalRequestDto.getEmail());
            ResponseDto responseDto = new ResponseDto(null, taskList, null, ResponseCode.GET_TASK_LIST_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_TASK_LIST_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-task-project-id")
    public ResponseEntity<ResponseDto> getTaskByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            List<TaskDto> taskList = manageEngineAPIService.getTaskByProjectId(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, taskList, null, ResponseCode.GET_TASK_LIST_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_TASK_LIST_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-project-by-id")
    public ResponseEntity<ResponseDto> getProjectById(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            ProjectTemplateDto projectDto = manageEngineAPIService.getProjectById(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, projectDto, null, ResponseCode.GET_PROJECT_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_ERROR);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-owner-by-id")
    public ResponseEntity<ResponseDto> getOwnerByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            List<OwnerDto> owners = manageEngineAPIService.getOwners(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, owners, null, ResponseCode.GET_TASK_OWNERS_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_OWNER_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-priority-by-id")
    public ResponseEntity<ResponseDto> getPriorityByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            List<PriorityDto> priorityList = manageEngineAPIService.getProjectPriority(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, priorityList, null, ResponseCode.GET_PRIORITY_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PRIORITY_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/get-status-by-id")
    public ResponseEntity<ResponseDto> getStatusByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            List<StatusDto> priorityList = manageEngineAPIService.getProjectStatus(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, priorityList, null, ResponseCode.GET_STATUS_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_STATUS_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/projects/{projectId}/add-task")
    public ResponseEntity<ResponseDto> addTask(@PathVariable String projectId, @RequestBody AddTaskDto newTask, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
        try {
            AddTaskResponse addedTask = manageEngineAPIService.addTask(refreshToken, projectId, newTask);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.TASK_ADD_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.TASK_ADD_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
