package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_requests")
@Data
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;

    @Column(name = "request_description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "user_id")
    private User author;

    @Column(name = "created")
    private LocalDateTime created;

    public ItemRequest() {
    }

    public ItemRequest(Long id, String description, User author, LocalDateTime created) {
        this.id = id;
        this.description = description;
        this.author = author;
        this.created = created;
    }
}
