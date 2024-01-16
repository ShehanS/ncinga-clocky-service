package com.ncinga.timer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncinga.timer.dtos.queryDto.ListInfo;
import com.ncinga.timer.dtos.queryDto.QueryRequest;
import com.ncinga.timer.dtos.responseDto.TaskListResponseDto;
import com.ncinga.timer.exceptions.RefreshTokenHasExpired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class QueryService {

    private static RestTemplate restTemplate = null;

    public QueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static Object executeHTTPRequest(String refreshToken, QueryRequest queryData, String url) throws RefreshTokenHasExpired, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String query = objectMapper.writeValueAsString(queryData);
        URI uri = UriComponentsBuilder.fromUriString(url)
                .queryParam("input_data", query)
                .build()
                .encode()
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
}
