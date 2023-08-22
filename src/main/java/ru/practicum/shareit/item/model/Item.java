package ru.practicum.shareit.item.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "items")
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "item_description", nullable = false)
    private String description;

    @Column(name = "is_available")
    private Boolean available;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "user_id")
    private User owner;

    @OneToMany(targetEntity = Booking.class,
                cascade = CascadeType.ALL,
                mappedBy = "item",
                fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @Column(name = "last_booking_id")
    private Long lastBooking;

    @Column(name = "next_booking_id")
    private Long nextBooking;

    @OneToMany(targetEntity = Comment.class,
                cascade = CascadeType.ALL,
                mappedBy = "item",
                fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @Column(name = "request_id")
    private Long requestId;

    public Item() {
    }

    public Item(Long id, String name, String description, Boolean available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
