package ru.practicum.shareit.booking.client;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {

    private static final java.lang.String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") java.lang.String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getAllBookings(long userId, @NotNull State state, Integer from, Integer size) {
        Map<java.lang.String, Object> parameter = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameter);
    }

    public ResponseEntity<Object> getAllBookingsForOwner(long userId, @NotNull State state, Integer from, Integer size) {
        Map<java.lang.String, Object> parameter = Map.of(
                "state", state.name(),
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameter);
    }

    public ResponseEntity<Object> addBooking(long userId, BookingDto dto) {
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> setApproved(long userId, long bookingId, Boolean isApproved) {
        Map<java.lang.String, Object> parameter = Map.of(
                "approved", isApproved
        );
        return patch("/" + bookingId, userId, parameter);
    }
}
