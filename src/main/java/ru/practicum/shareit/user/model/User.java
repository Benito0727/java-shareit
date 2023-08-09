package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.Set;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email",nullable = false, unique = true)
    private String email;

    @OneToMany(targetEntity = Item.class,
                mappedBy = "owner",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private Set<Item> items;

    @OneToMany(targetEntity = Booking.class,
                mappedBy = "booker",
                fetch = FetchType.LAZY,
                cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
