package ru.practicum.shareit.client;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;

public class BaseClient {

    protected final RestTemplate restTemplate;

    public BaseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static ResponseEntity<Object> prepareGatewayResponse(ResponseEntity<Object> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            return response;
        }

        ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.status(response.getStatusCode());

        if (response.hasBody()) {
            return responseBuilder.body(response.getBody());
        }

        return responseBuilder.build();
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        headers.setAccept(List.of(APPLICATION_JSON));

        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }

        return headers;
    }

    private <T> ResponseEntity<Object> makeAndSendRequest(HttpMethod method,
                                                          String path,
                                                          Long userId,
                                                          @Nullable Map<String, Object> parameters,
                                                          @Nullable T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, defaultHeaders(userId));

        ResponseEntity<Object> serverResponse;
        try {
            if (parameters != null) {
                serverResponse = restTemplate.exchange(path, method, requestEntity, Object.class, parameters);
            } else {
                serverResponse = restTemplate.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsByteArray());
        }

        return prepareGatewayResponse(serverResponse);
    }

    protected ResponseEntity<Object> get(String path, Long userId, @Nullable Map<String, Object> parameter) {
        return makeAndSendRequest(GET, path, userId, parameter, null);
    }

    protected ResponseEntity<Object> get(String path, Long userId) {
        return get(path, userId, null);
    }

    protected ResponseEntity<Object> get(String path) {
        return get(path, null);
    }

    protected <T> ResponseEntity<Object> post(String path,
                                              Long userId,
                                              @Nullable Map<String, Object> parameter,
                                              T body) {
        return makeAndSendRequest(POST, path, userId, parameter, body);
    }

    protected <T> ResponseEntity<Object> post(String path,
                                              Long userId,
                                              T body) {
        return post(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> post(String path,
                                              T body) {
        return post(path, null, body);
    }

    protected <T> ResponseEntity<Object> put(String path,
                                             Long userId,
                                             @Nullable Map<String, Object> parameter,
                                             T body) {
        return makeAndSendRequest(PUT, path, userId, parameter, body);
    }

    protected <T> ResponseEntity<Object> put(String path,
                                             Long userId,
                                             T body) {
        return put(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path,
                                               Long userId,
                                               @Nullable Map<String, Object> parameter,
                                               T body) {
        return makeAndSendRequest(PATCH, path, userId, parameter, body);
    }

    protected <T> ResponseEntity<Object> patch(String path,
                                               Long userId,
                                               @Nullable Map<String, Object> parameter) {
        return patch(path, userId, parameter, null);
    }

    protected <T> ResponseEntity<Object> patch(String path,
                                               Long userId,
                                               T body) {
        return patch(path, userId, null, body);
    }

    protected <T> ResponseEntity<Object> patch(String path,
                                               Long userId) {
        return patch(path, userId, null);
    }

    protected <T> ResponseEntity<Object> patch(String path,
                                               T body) {
        return patch(path, null, body);
    }

    protected ResponseEntity<Object> delete(String path,
                                            Long userId,
                                            Map<String, Object> parameter) {
        return makeAndSendRequest(DELETE, path, userId, parameter, null);
    }

    protected ResponseEntity<Object> delete(String path,
                                            Long userId) {
        return delete(path, userId, null);
    }

    protected ResponseEntity<Object> delete(String path) {
        return delete(path, null);
    }
}
