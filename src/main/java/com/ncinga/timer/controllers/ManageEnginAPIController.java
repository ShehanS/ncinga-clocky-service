package com.ncinga.timer.controllers;

import com.ncinga.timer.dtos.requestDto.*;
import com.ncinga.timer.dtos.responseDto.*;
import com.ncinga.timer.exceptions.RefreshTokenHasExpired;
import com.ncinga.timer.service.ManageEngineAPIService;
import com.ncinga.timer.utilities.ResponseCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "api/engine")
public class ManageEnginAPIController {

    @Autowired
    private ManageEngineAPIService manageEngineAPIService;

    @GetMapping(path = "/tasks")
    public ResponseEntity<List<TaskDto>> getTasks(@RequestParam String projectId, @RequestParam String email) {
        try {
            List<TaskDto> tasks = manageEngineAPIService.getTasks(projectId, email);
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/tasks")
    public ResponseEntity<TaskDto> createTask(@RequestHeader(value = "Authorization", required = false) String refreshToken, @RequestBody TaskDto newTask) {
        try {

            TaskDto createdTask = manageEngineAPIService.createTask(refreshToken, newTask);

            if (createdTask != null) {
                return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
            }

            else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "/tasks/{taskId}")
    public ResponseEntity<TaskDto> editTask(@PathVariable String taskId, @RequestBody TaskDto updatedTask) {
        try {
            TaskDto editedTask = manageEngineAPIService.editTask(taskId, updatedTask);

            if (editedTask != null) {
                return new ResponseEntity<>(editedTask, HttpStatus.OK);
            }

            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/tasks/{taskId}")
    public ResponseEntity<TaskDto> deleteTask(@RequestHeader(value = "Authorization", required = false) String refreshToken,@PathVariable String taskId) {
        try {
            TaskDto isDeleted = manageEngineAPIService.deleteTask(refreshToken,taskId);

            if (isDeleted != null) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }

        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping(path = "/get-projects")
    public ResponseEntity<ResponseDto> getProjects(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }

        try {
            List<ProjectDto> projectDtoList = manageEngineAPIService.getProjectList(refreshToken, generalRequestDto.getEmail());
            ResponseDto responseDto = new ResponseDto(null, projectDtoList, null, ResponseCode.GET_PROJECT_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_ERROR);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-project-owners")
    public ResponseEntity<ResponseDto> getProjectsOwners(@RequestBody OwnerListRequestDto ownerListRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            OwnerResponseDto owners = manageEngineAPIService.getProjectOwners(refreshToken, ownerListRequestDto);
            ResponseDto responseDto = new ResponseDto(null, owners, null, ResponseCode.GET_PROJECT_OWNER_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_OWNER_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-task-by-mail")
    public ResponseEntity<ResponseDto> getTaskByEmail(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            List<TaskDto> taskList = manageEngineAPIService.getTaskByEmail(refreshToken, generalRequestDto.getEmail());
            ResponseDto responseDto = new ResponseDto(null, taskList, null, ResponseCode.GET_TASK_LIST_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_TASK_LIST_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-task-project-id")
    public ResponseEntity<ResponseDto> getTaskByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            List<TaskDto> taskList = manageEngineAPIService.getTaskByProjectId(refreshToken, generalRequestDto.getProjectId(), generalRequestDto.getEmail());
            ResponseDto responseDto = new ResponseDto(null, taskList, null, ResponseCode.GET_TASK_LIST_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_TASK_LIST_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-project-by-id")
    public ResponseEntity<ResponseDto> getProjectById(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            ProjectTemplateDto projectDto = manageEngineAPIService.getProjectById(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, projectDto, null, ResponseCode.GET_PROJECT_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_ERROR);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-owner-by-id")
    public ResponseEntity<ResponseDto> getOwnerByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            List<OwnerDto> owners = manageEngineAPIService.getOwners(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, owners, null, ResponseCode.GET_TASK_OWNERS_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PROJECT_OWNER_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-priority-by-id")
    public ResponseEntity<ResponseDto> getPriorityByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            List<PriorityDto> priorityList = manageEngineAPIService.getProjectPriority(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, priorityList, null, ResponseCode.GET_PRIORITY_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_PRIORITY_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/get-status-by-id")
    public ResponseEntity<ResponseDto> getStatusByProjectId(@RequestBody GeneralRequestDto generalRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            List<StatusDto> priorityList = manageEngineAPIService.getProjectStatus(refreshToken, generalRequestDto.getProjectId());
            ResponseDto responseDto = new ResponseDto(null, priorityList, null, ResponseCode.GET_STATUS_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_STATUS_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/projects/{projectId}/add-task")
    public ResponseEntity<ResponseDto> addTask(@PathVariable String projectId, @Valid @RequestBody AddEditTaskDto newTask, BindingResult result, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        if (result.hasErrors()) {
            List<String> errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            ResponseDto response = new ResponseDto(null, null, errors, ResponseCode.TASK_ADD_FAILED);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        try {
            ProjectTask addedTask = manageEngineAPIService.addTask(refreshToken, projectId, newTask);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.TASK_ADD_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.TASK_ADD_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<ResponseDto> updateTask(@PathVariable String projectId, @PathVariable String taskId, @RequestBody AddEditTaskDto updateTask, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            ProjectTask addedTask = manageEngineAPIService.updateTask(refreshToken, projectId, taskId, updateTask);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.TASK_UPDATED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.TASK_UPDATE_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<ResponseDto> getTaskById(@PathVariable String projectId, @PathVariable String taskId, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            ProjectTask addedTask = manageEngineAPIService.getTask(refreshToken, projectId, taskId);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.GET_TASK_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_TASK_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @DeleteMapping (path = "/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<ResponseDto> deleteTaskById(@PathVariable String projectId, @PathVariable String taskId, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            DeleteTaskDto deleteTask = manageEngineAPIService.deleteTask(refreshToken, projectId, taskId);
            ResponseDto responseDto = new ResponseDto(null, deleteTask, null, ResponseCode.DELETE_TASK_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.DELETE_TASK_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/projects/{projectId}/tasks/{taskId}/add-worklog")
    public ResponseEntity<ResponseDto> addWorklog(@PathVariable String projectId, @PathVariable String taskId, @RequestBody WorkLogRequestDto workLogRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            WorkLogResponseDto addedTask = manageEngineAPIService.addWorkLog(refreshToken, projectId, taskId, workLogRequestDto);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.WORKLOG_ADD_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.WORKLOG_ADD_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/projects/{projectId}/tasks/{taskId}/worklogs/{worklogId}/edit-worklog")
    public ResponseEntity<ResponseDto> editWorklog(@PathVariable String projectId, @PathVariable String taskId, @PathVariable String worklogId, @RequestBody WorkLogRequestDto workLogRequestDto, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            WorkLogResponseDto addedTask = manageEngineAPIService.updateWorkLog(refreshToken, projectId, taskId, worklogId, workLogRequestDto);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.WORKLOG_EDIT_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.WORKLOG_EDIT_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/projects/{projectId}/tasks/{taskId}/worklogs/{worklogId}/delete")
    public ResponseEntity<ResponseDto> deleteWorklog(@PathVariable String projectId, @PathVariable String taskId, @PathVariable String worklogId, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            WorkLogResponseDto addedTask = manageEngineAPIService.deleteWorklog(refreshToken, projectId, taskId, worklogId);
            ResponseDto responseDto = new ResponseDto(null, addedTask, null, ResponseCode.WORKLOG_DELETE_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.WORKLOG_DELETE_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }


    @GetMapping(path = "/projects/{projectId}/tasks/{taskId}/worklogs")
    public ResponseEntity<ResponseDto> getWorkloads(@PathVariable String projectId, @PathVariable String taskId, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            WorkLogResponseDto workLogDtoList = manageEngineAPIService.getWorkLogs(refreshToken, projectId, taskId);
            ResponseDto responseDto = new ResponseDto(null, workLogDtoList, null, ResponseCode.GET_ALL_WORKLOG_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_ALL_WORKLOG_FAILED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/projects/{projectId}/tasks/{taskId}/worklog-types")
    public ResponseEntity<ResponseDto> getWorklogTypes(@PathVariable String projectId, @PathVariable String taskId, @RequestHeader(value = "Authorization", required = false) String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            ResponseDto responseDto = new ResponseDto(null, null, "Authorization key is null or empty", ResponseCode.AUTHORIZATION_TOKEN_NULL);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
        try {
            WorkLogTypeResponseDto workLogDtoList = manageEngineAPIService.getTypes(refreshToken, projectId, taskId);
            ResponseDto responseDto = new ResponseDto(null, workLogDtoList, null, ResponseCode.GET_ALL_TYPE_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (RefreshTokenHasExpired e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.REFRESH_TOKEN_HAS_EXPIRED);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDto responseDto = new ResponseDto(null, null, e.getMessage(), ResponseCode.GET_ALL_TYPE_SUCCESS);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        }
    }



}
