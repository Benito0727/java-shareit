package ru.practicum.shareit.booking.client;

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
import ru.practicum.shareit.exception.ValidationException;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
        validateBookingDto(dto);
        return post("", userId, dto);
    }

    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> setApproved(long userId, long bookingId, Boolean isApproved) {
        Map<java.lang.String, Object> parameter = Map.of(
                "approved", isApproved
        );
        return patch("/" + bookingId + "?approved={approved}", userId, parameter);
    }

    private void validateBookingDto(BookingDto dto) {
        try {
            if (dto.getStart().isAfter(dto.getEnd())) {
                throw new ValidationException("Дата и время старта должны быть раньше даты и времени окончания");
            }
            if (dto.getStart().equals(dto.getEnd())) {
                throw new ValidationException("Дата и время старта и окончания должны отличаться");
            }
            if (dto.getStart().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата и время старта должны бють в будущем");
            }
        } catch (ValidationException exception) {
            throw new RuntimeException(exception);
        }
    }
}
