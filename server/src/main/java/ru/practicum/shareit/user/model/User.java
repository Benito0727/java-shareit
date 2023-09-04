package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Set;

@Getter
@Setter
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
                fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    private Set<Item> items;

    @OneToMany(targetEntity = Booking.class,
                mappedBy = "booker",
                fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    private Set<Booking> bookings;

    @OneToMany(targetEntity = ItemRequest.class,
                mappedBy = "author",
                fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    private List<ItemRequest> requests;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
