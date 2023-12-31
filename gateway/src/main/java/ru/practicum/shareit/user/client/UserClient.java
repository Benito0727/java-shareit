package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDto;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUri, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUri + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addUser(UserDto dto) {
        return post("", dto);
    }

    public ResponseEntity<Object> getUserById(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> updateUser(long userId, UserDto dto) {
        return patch("/" + userId, dto);
    }

    public ResponseEntity<Object> deleteUser(long userId) {
        return delete("/" + userId);
    }

    public ResponseEntity<Object> getAllUsers() {
        return get("");
    }
}
