package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.State;

import java.util.Map;

@Service
public class RequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public RequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(long userId, ItemRequestDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getUserRequests(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameter = getParameter(state, from, size);
        return get("?state={state}&from={from}&size={size}", userId, parameter);
    }

    public ResponseEntity<Object> getOtherUserRequests(long userId, State state, Integer from, Integer size) {
        Map<String, Object> parameter = getParameter(state, from, size);
        return get("/all?state={state}&from={from}&size={size}", userId, parameter);
    }

    public ResponseEntity<Object> getRequestById(long userId, long requestId) {
        return get("/" + requestId, userId);
    }

    private Map<String, Object> getParameter(State state, Integer from, Integer size) {
        return Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
    }
}
