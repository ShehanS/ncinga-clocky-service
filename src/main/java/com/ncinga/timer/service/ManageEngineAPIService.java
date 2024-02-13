package com.ncinga.timer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ncinga.timer.dtos.queryDto.ListInfo;
import com.ncinga.timer.dtos.queryDto.QueryRequest;
import com.ncinga.timer.dtos.queryDto.SearchCriteria;
import com.ncinga.timer.dtos.requestDto.*;
import com.ncinga.timer.dtos.responseDto.*;
import com.ncinga.timer.exceptions.RefreshTokenHasExpired;
import com.ncinga.timer.interfacrs.IManageEngine;
import com.ncinga.timer.utilities.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.ncinga.timer.dtos.requestDto.TaskDTO;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.ncinga.timer.dtos.requestDto.TaskDTO.logger;

@Service
public class ManageEngineAPIService implements IManageEngine {
    @Value("${engine.url}")
    private String API;
    private final RestTemplate restTemplate;

    public ManageEngineAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TaskDTO.Task> getTaskList(String refreshToken) {         //getting all tasks
        List<TaskDTO.Task> tasks = new ArrayList<>();
        try {
            String taskUrl = API + "/tasks";

            SearchCriteria moduleCriteria = new SearchCriteria();
            moduleCriteria.setField("module");
            moduleCriteria.setCondition("is");
            moduleCriteria.setValue("General");

            List<SearchCriteria> criteria = new ArrayList<>();
            criteria.add(moduleCriteria);

            ListInfo listInfo = new ListInfo();
            listInfo.setSearch_criteria(criteria);
            listInfo.setRow_count(100);

            com.ncinga.timer.dtos.queryDto.QueryRequest queryRequest =
                    new com.ncinga.timer.dtos.queryDto.QueryRequest(listInfo);

            Object taskResponse = QueryService.executeHTTPRequest(refreshToken, queryRequest, taskUrl);

            ObjectMapper objectMapper = new ObjectMapper();
            String tasksString = objectMapper.writeValueAsString(taskResponse);
            JsonNode tasksResponseNode = objectMapper.readTree(tasksString);

            JsonNode tasksNode = tasksResponseNode.get("tasks");
            if (tasksNode != null && tasksNode.isArray()) {
                for (JsonNode taskNode : tasksNode) {
                    TaskDTO.Task taskDto = objectMapper.convertValue(taskNode, TaskDTO.Task.class);
                    taskDto.setAssociated_entity("task");
                    tasks.add(taskDto);
                }
            }

        } catch (HttpClientErrorException ex) {
            logger.error("HTTP client error. Status code: {}, Response: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
        } catch (JsonProcessingException | RefreshTokenHasExpired e) {
            logger.error("JSON processing error or refresh token expired: ", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred: ", e);
        }

        return tasks;
    }




    public Object getTaskById(String refreshToken, String taskId) throws RefreshTokenHasExpired {
        String taskUrl = API + "/tasks/" + taskId;
        URI uri = UriComponentsBuilder.fromUriString(taskUrl)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Object> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    Object.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    public TaskDTO.Task createTask(String refreshToken, TaskDTO.Task newTask) throws JsonProcessingException, RefreshTokenHasExpired {
        String createTaskApiUrl = API + "/tasks";
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(newTask);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

        try {
            ResponseEntity<TaskDTO.Task> responseEntity = restTemplate.exchange(
                    createTaskApiUrl,
                    HttpMethod.GET,
                    requestEntity,
                    TaskDTO.Task.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, please refresh page");
            } else {
                throw e;
            }
        }
    }


    public TaskDTO.Task updateTask(String refreshToken, String taskId, TaskDTO.Task updatedTask) throws RefreshTokenHasExpired, JsonProcessingException {
        String updateTaskApiUrl = API + "/update-task/" + taskId;
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(updatedTask);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonString, headers);

        try {
            ResponseEntity<TaskDTO.Task> responseEntity = restTemplate.exchange(
                    updateTaskApiUrl,
                    HttpMethod.PUT,
                    requestEntity,
                    TaskDTO.Task.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, please refresh page");
            } else {
                throw e;
            }
        }
    }


    public TaskDTO.Task deleteTask(String refreshToken, String taskId) throws JsonProcessingException, RefreshTokenHasExpired {
        String deleteTaskApiUrl = API + "/tasks/" + taskId;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/json");
        headers.set("Accept", "application/json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TaskDTO.Task> responseEntity = restTemplate.exchange(
                    deleteTaskApiUrl,
                    HttpMethod.DELETE,
                    requestEntity,
                    TaskDTO.Task.class
            );
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatusCode statusCode = e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }



    public List<ProjectDto> getProjectList(String refreshToken, String email) throws RefreshTokenHasExpired, JsonProcessingException {
        String taskApiUrl = API + "/tasks";
        String projectApiUrl = API + "/projects";
        ObjectMapper objectMapper = new ObjectMapper();
        List<ProjectDto> filterProjects = new ArrayList<>();
        List<JsonNode> taskList = new ArrayList<>();
        List<JsonNode> projectList = new ArrayList<>();
        boolean hasMoreTask = true;
        int taskIndex = 1;
        boolean hasMoreProject = true;
        int projectIndex = 1;

        while (hasMoreTask) {
            SearchCriteria taskOwner = new SearchCriteria();
            taskOwner.setField("owner.email_id");
            taskOwner.setCondition("is");
            taskOwner.setValue(email);
            SearchCriteria taskStatus = new SearchCriteria();
            taskStatus.setField("status.name");
            taskStatus.setCondition("is not");
            taskStatus.setLogical_operator("and");
            taskStatus.setValue("Closed");
            List<SearchCriteria> criteria = new ArrayList<>();
            criteria.add(taskOwner);
            criteria.add(taskStatus);
            ListInfo listInfo = new ListInfo();
            listInfo.setSearch_criteria(criteria);
            listInfo.setStart_index(taskIndex);
            listInfo.setRow_count(100);
            QueryRequest queryRequest = new QueryRequest(listInfo);
            Object taskResponse = QueryService.executeHTTPRequest(refreshToken, queryRequest, taskApiUrl);
            String tasksString = objectMapper.writeValueAsString(taskResponse);
            JsonNode tasksResponseNode = objectMapper.readTree(tasksString);
            hasMoreTask = tasksResponseNode.get("list_info").get("has_more_rows").asBoolean();
            taskIndex++;
            JsonNode tasks = tasksResponseNode.get("tasks");
            for (JsonNode task : tasks) {
                taskList.add(task);
            }

        }
        while (hasMoreProject) {
            SearchCriteria projectStatus = new SearchCriteria();
            projectStatus.setField("status.name");
            projectStatus.setCondition("is not");
            projectStatus.setValue("Closed");
            List<SearchCriteria> criteria = new ArrayList<>();
            criteria.add(projectStatus);
            ListInfo listInfo = new ListInfo();
            listInfo.setStart_index(projectIndex);
            listInfo.setSearch_criteria(criteria);
            listInfo.setRow_count(100);
            QueryRequest queryRequest = new QueryRequest(listInfo);
            Object projectsResponse = QueryService.executeHTTPRequest(refreshToken, queryRequest, projectApiUrl);
            String projectsString = objectMapper.writeValueAsString(projectsResponse);
            JsonNode projectResponseNode = objectMapper.readTree(projectsString);
            hasMoreProject = projectResponseNode.get("list_info").get("has_more_rows").asBoolean();
            projectIndex++;
            JsonNode projects = projectResponseNode.get("projects");
            for (JsonNode project : projects) {
                projectList.add(project);
            }


        }
        for (JsonNode project : projectList) {
            String projectId = project.get("id").asText();
            for (JsonNode task : taskList) {
                String taskProjectId = task.get("project").get("id").asText();
                if (projectId.equals(taskProjectId)) {
                    ProjectDto projectDto = objectMapper.convertValue(project, ProjectDto.class);
                    if (filterProjects.stream().anyMatch(p -> p.getTitle().equals(projectDto.getTitle()))) {
                    } else {
                        filterProjects.add(projectDto);
                    }
                }
            }

        }

        return filterProjects;
    }



    @Override
    public ProjectTemplateDto getProjectById(String refreshToken, String projectId) throws RefreshTokenHasExpired {
        String apiUrl = API + "/projects/" + projectId;
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{}")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProjectTemplateDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    ProjectTemplateDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();


        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public OwnerResponseDto getProjectOwners(String refreshToken, OwnerListRequestDto ownerListRequestDto) throws RefreshTokenHasExpired {
        String apiUrl = API + "/projects/" + ownerListRequestDto.getProjectId() + "/tasks/owner";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .build()
                .encode()
                .toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<OwnerDto> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<OwnerResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    OwnerResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<TaskDto> getTaskByEmail(String refreshToken, String email) throws RefreshTokenHasExpired {
        String apiUrl = API + "/tasks";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{\n" +
                        "  \"list_info\": {\n" +
                        "    \"row_count\": 5000,\n" +
                        "    \"search_criteria\": [\n" +
                        "      {\n" +
                        "        \"field\": \"owner.email_id\",\n" +
                        "        \"condition\": \"is\",\n" +
                        "        \"value\": \"" + email + "\"\n" +
                        "      }\n, \n" +
                        "    ]\n" +
                        "  }\n" +
                        "}")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TaskListResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    TaskListResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();

            return responseEntity.getBody().getTasks();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<TaskDto> getTaskByProjectId(String refreshToken, String projectId, String email) throws RefreshTokenHasExpired, JsonProcessingException {
        String taskApiUrl = API + "/projects/" + projectId + "/tasks";
        ObjectMapper objectMapper = new ObjectMapper();
        List<TaskDto> filterTask = new ArrayList<>();
        List<JsonNode> taskList = new ArrayList<>();
        boolean hasMoreTask = true;
        int taskIndex = 1;

        while (hasMoreTask) {
            SearchCriteria taskOwner = new SearchCriteria();
            taskOwner.setField("owner.email_id");
            taskOwner.setCondition("is");
            taskOwner.setValue(email);
            SearchCriteria taskStatus = new SearchCriteria();
            taskStatus.setField("status.name");
            taskStatus.setCondition("is not");
            taskStatus.setLogical_operator("and");
            taskStatus.setValue("Closed");
            List<SearchCriteria> criteria = new ArrayList<>();
            criteria.add(taskOwner);
            criteria.add(taskStatus);
            ListInfo listInfo = new ListInfo();
            listInfo.setSearch_criteria(criteria);
            listInfo.setStart_index(taskIndex);
            listInfo.setRow_count(100);
            QueryRequest queryRequest = new QueryRequest(listInfo);
            Object taskResponse = QueryService.executeHTTPRequest(refreshToken, queryRequest, taskApiUrl);
            String tasksString = objectMapper.writeValueAsString(taskResponse);
            JsonNode tasksResponseNode = objectMapper.readTree(tasksString);
            hasMoreTask = tasksResponseNode.get("list_info").get("has_more_rows").asBoolean();
            taskIndex++;
            JsonNode tasks = tasksResponseNode.get("tasks");
            for (JsonNode task : tasks) {
                taskList.add(task);
            }
        }
        if (taskList.size() > 0) {
            for (JsonNode task : taskList) {
                TaskDto taskDto = objectMapper.convertValue(task, TaskDto.class);
                if (!taskDto.getTitle().equals("Time-Sheet-Task")) {
                    filterTask.add(taskDto);
                }

            }
        }

        return filterTask;
    }

    @Override
    public List<OwnerDto> getOwners(String refreshToken, String projectId) throws RefreshTokenHasExpired {
        String apiUrl = API + "/projects/" + projectId + "/tasks/owner";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{\n" +
                        "  \"list_info\": {\n" +
                        "    \"start_index\": 0,\n" +
                        "    \"row_count\": 1000,\n" +
                        "    \"search_criteria\": {\n" +
                        "      \"field\": \"name\",\n" +
                        "      \"condition\": \"like\",\n" +
                        "      \"values\": [\n" +
                        "        \"\"\n" +
                        "      ],\n" +
                        "      \"children\": [\n" +
                        "        {\n" +
                        "          \"field\": \"email_id\",\n" +
                        "          \"condition\": \"like\",\n" +
                        "          \"value\": \"\",\n" +
                        "          \"logical_operator\": \"or\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<OwnerResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    OwnerResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody().getOwner();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<PriorityDto> getProjectPriority(String refreshToken, String projectId) throws RefreshTokenHasExpired {
        String apiUrl = API + "/projects/" + projectId + "/tasks/priority";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{\n" +
                        "  \"list_info\": {\n" +
                        "    \"start_index\": 0,\n" +
                        "    \"row_count\": 1000,\n" +
                        "    \"search_criteria\": {\n" +
                        "      \"field\": \"name\",\n" +
                        "      \"condition\": \"like\",\n" +
                        "      \"values\": [\n" +
                        "        \"\"\n" +
                        "      ],\n" +
                        "      \"children\": [\n" +
                        "        {\n" +
                        "          \"field\": \"email_id\",\n" +
                        "          \"condition\": \"like\",\n" +
                        "          \"value\": \"\",\n" +
                        "          \"logical_operator\": \"or\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<PriorityRequestDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    PriorityRequestDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody().getPriority();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<StatusDto> getProjectStatus(String refreshToken, String projectId) throws RefreshTokenHasExpired {
        String apiUrl = API + "/projects/" + projectId + "/tasks/status";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{\n" +
                        "  \"list_info\": {\n" +
                        "    \"start_index\": 0,\n" +
                        "    \"row_count\": 1000,\n" +
                        "    \"search_criteria\": {\n" +
                        "      \"field\": \"name\",\n" +
                        "      \"condition\": \"like\",\n" +
                        "      \"values\": [\n" +
                        "        \"\"\n" +
                        "      ],\n" +
                        "      \"children\": [\n" +
                        "        {\n" +
                        "          \"field\": \"email_id\",\n" +
                        "          \"condition\": \"like\",\n" +
                        "          \"value\": \"\",\n" +
                        "          \"logical_operator\": \"or\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<StatusRequestDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    StatusRequestDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody().getStatus();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public WorkLogResponseDto addWorkLog(String refreshToken, String projectId, String taskId, WorkLogRequestDto workLogRequestDto) throws RefreshTokenHasExpired, JsonProcessingException {
        long currentEpoch = Instant.now().getEpochSecond() * 1000;
        Long differenceInDays = Validator.calculateDifferenceInDays(currentEpoch, Long.parseLong(workLogRequestDto.getWorklog().start_time.getValue()));
        if(differenceInDays >= 15){
            throw new RuntimeException("Maximum input date range should be below 15 days");
        }
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId + "/worklogs";
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(workLogRequestDto);
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", jsonString)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkLogResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    WorkLogResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public WorkLogResponseDto updateWorkLog(String refreshToken, String projectId, String taskId, String worklogId, WorkLogRequestDto workLogRequestDto) throws RefreshTokenHasExpired, JsonProcessingException {
        long currentEpoch = Instant.now().getEpochSecond() * 1000;
        Long differenceInDays = Validator.calculateDifferenceInDays(currentEpoch, Long.parseLong(workLogRequestDto.getWorklog().start_time.getValue()));
        if(differenceInDays >= 15){
            throw new RuntimeException("Maximum input date range should be below 15 days");
        }
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId + "/worklogs/" + worklogId;
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(workLogRequestDto);
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", jsonString)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkLogResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    WorkLogResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public WorkLogResponseDto getWorkLogs(String refreshToken, String projectId, String taskId) throws RefreshTokenHasExpired {
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId + "/worklogs";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{ \n" +
                        "        \"list_info\" : { \n" +
                        "            \"row_count\" : 100\n" +
                        "        } \n" +
                        "      }")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkLogResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    WorkLogResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public WorkLogResponseDto deleteWorklog(String refreshToken, String projectId, String taskId, String worklogId) throws RefreshTokenHasExpired, JsonProcessingException {
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId + "/worklogs/" + worklogId;
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkLogResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.DELETE,
                    requestEntity,
                    WorkLogResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public ProjectTask addTask(String refreshToken, String projectId, AddEditTaskDto task) throws RefreshTokenHasExpired, JsonProcessingException {
        long currentEpoch = Instant.now().getEpochSecond() * 1000;
        Long differenceInDays = Validator.calculateDifferenceInDays(currentEpoch, Long.parseLong(task.getTask().getActual_start_time().getValue()));
        if(differenceInDays >= 15){
            throw new RuntimeException("Maximum input date range should be below 15 days");
        }
        String apiUrl = API + "/projects/" + projectId + "/tasks";
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(task);
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", jsonString)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProjectTask> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    ProjectTask.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public ProjectTask updateTask(String refreshToken, String projectId, String taskId, AddEditTaskDto task) throws RefreshTokenHasExpired, JsonProcessingException {
        long currentEpoch = Instant.now().getEpochSecond() * 1000;
        Long differenceInDays = Validator.calculateDifferenceInDays(currentEpoch, Long.parseLong(task.getTask().getActual_start_time().getValue()));
        String jsonString = "";
        if (differenceInDays >= 15) {
            throw new RuntimeException("Maximum input date range should be below 15 days");
        }
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId;
        ObjectMapper objectMapper = new ObjectMapper();
        if (task.getTask().getStatus().getName().equals("Closed")) {
            //{"task":{"status":{"id":"143153000000006661"}}}
            ObjectNode rootNode = objectMapper.createObjectNode();
            ObjectNode taskNode = rootNode.putObject("task");
            ObjectNode statusNode = taskNode.putObject("status");
            statusNode.put("id", task.getTask().getStatus().getId());
            jsonString = objectMapper.writeValueAsString(rootNode);
        } else {
            jsonString = objectMapper.writeValueAsString(task);
        }

        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", jsonString)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProjectTask> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.PUT,
                    requestEntity,
                    ProjectTask.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public ProjectTask getTask(String refreshToken, String projectId, String taskId) throws RefreshTokenHasExpired, JsonProcessingException {
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId;
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<ProjectTask> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    ProjectTask.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

    @Override
    public DeleteTaskDto deleteTask(String refreshToken, String projectId, String taskId) throws RefreshTokenHasExpired, JsonProcessingException {
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId;
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<TaskDeleteResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.DELETE,
                    requestEntity,
                    TaskDeleteResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody().getResponse_status();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }


    public WorkLogTypeResponseDto getTypes(String refreshToken, String projectId, String taskId) throws RefreshTokenHasExpired, JsonProcessingException {
        String apiUrl = API + "/projects/" + projectId + "/tasks/" + taskId + "/worklogs/worklog_type";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{\n" +
                        "    \"list_info\": {\n" +
                        "        \"start_index\": 0,\n" +
                        "        \"row_count\": 50,\n" +
                        "        \"sort_field\": \"name\",\n" +
                        "        \"sort_order\": \"asc\",\n" +
                        "        \"search_criteria\": {\n" +
                        "            \"field\": \"name\",\n" +
                        "            \"condition\": \"like\",\n" +
                        "            \"values\": [\n" +
                        "                \"\"\n" +
                        "            ]\n" +
                        "        }\n" +
                        "    }\n" +
                        "}")
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", refreshToken);
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("Accept", "application/vnd.manageengine.sdp.v3+json");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<WorkLogTypeResponseDto> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    requestEntity,
                    WorkLogTypeResponseDto.class
            );
            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            return responseEntity.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            if (statusCode == HttpStatus.UNAUTHORIZED) {
                throw new RefreshTokenHasExpired("Your refresh token has expired, Please refresh page");
            } else {
                throw e;
            }
        }
    }

}