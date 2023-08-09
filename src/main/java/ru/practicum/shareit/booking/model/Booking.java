package ru.practicum.shareit.booking.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@QueryEntity
@Data
@Entity
@Table(name = "bookings")
@RequiredArgsConstructor
public class Booking {

    @Id
    @Column(name = "booking_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime start;

    @Column(name = "end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime end;

    @Column(name = "status_id")
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "booker_id", referencedColumnName = "user_id")
    private User booker;

    @Column(name = "item_id")
    private Long itemId;

    public Booking(Long itemId, LocalDateTime start, LocalDateTime end) {
        this.itemId = itemId;
        this.start = start;
        this.end = end;
    }
}
