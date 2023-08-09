package ru.practicum.shareit.item.model;

import lombok.Data;

import jakarta.persistence.*;

@Data
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

    @Column(name = "owner_id")
    private Long owner;

    @Column(name = "last_booking_id")
    private Long lastBookingId;

    @Column(name = "next_booking_id")
    private Long nextBookingId;

    public Item() {
    }

    public Item(Long id, String name, String description, Boolean available, Long owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}
