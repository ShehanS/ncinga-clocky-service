package com.ncinga.timer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncinga.timer.dtos.requestDto.*;
import com.ncinga.timer.dtos.responseDto.OwnerResponseDto;
import com.ncinga.timer.dtos.responseDto.TaskDto;
import com.ncinga.timer.dtos.responseDto.TaskListResponseDto;
import com.ncinga.timer.exceptions.RefreshTokenHasExpired;
import com.ncinga.timer.interfacrs.IManageEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManageEngineAPIService implements IManageEngine {
    @Value("${engine.url}")
    private String API;
    private final RestTemplate restTemplate;

    public ManageEngineAPIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ProjectDto> getProjectList(String refreshToken, String email) throws RefreshTokenHasExpired {
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
                        "      }\n" +
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
            TaskListResponseDto taskListResponseDto = responseEntity.getBody();

            List<ProjectDto> projectDtoList = taskListResponseDto.getTasks().stream().map(task -> task.getProject()).distinct().collect(Collectors.toList());
            return projectDtoList;

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
                        "      }\n" +
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
    public List<TaskDto> getTaskByProjectId(String refreshToken, String projectId) throws RefreshTokenHasExpired {
        String apiUrl = API + "/tasks";
        URI uri = UriComponentsBuilder.fromUriString(apiUrl)
                .queryParam("input_data", "{\n" +
                        "  \"list_info\": {\n" +
                        "    \"row_count\": 5000,\n" +
                        "    \"search_criteria\": [\n" +
                        "      {\n" +
                        "        \"field\": \"project.id\",\n" +
                        "        \"condition\": \"is\",\n" +
                        "        \"value\": \"" + projectId + "\"\n" +
                        "      }\n" +
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
    public AddTaskResponse addTask(String refreshToken, String projectId, AddTaskDto task) throws RefreshTokenHasExpired, JsonProcessingException {
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
            ResponseEntity<AddTaskResponse> responseEntity = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    requestEntity,
                    AddTaskResponse.class
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
