package ru.practicum.shareit.booking.dto;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Path;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

public class QBooking extends EntityPathBase<Booking> {

    private static final long serialVersionUID = 551248612L;

    public static final QBooking booking = new QBooking("booking");

    public final NumberPath<Long> booker = createNumber("booker", Long.class);

    public final DatePath<java.time.LocalDate> end = createDate("end", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> itemId = createNumber("itemId", Long.class);

    public final StringPath itemName = createString("itemName");

    public final DatePath<java.time.LocalDate> start = createDate("start", java.time.LocalDate.class);

    public final EnumPath<Status> status = createEnum("status", Status.class);

    public QBooking(String variable) {
        super(Booking.class, forVariable(variable));
    }

    public QBooking(Path<? extends Booking> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBooking(PathMetadata metadata) {
        super(Booking.class, metadata);
    }

}
