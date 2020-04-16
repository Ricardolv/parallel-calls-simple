package com.richard.service;

import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ObjectRequest {

    private final String url;
    private final RestTemplate restTemplate;
    private ResponseEntity responseEntity;

    public ObjectRequest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    public void get() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        this.responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }
}
