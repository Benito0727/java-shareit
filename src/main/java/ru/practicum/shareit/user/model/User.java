package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "user_email",nullable = false, unique = true)
    private String email;

    public User(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
