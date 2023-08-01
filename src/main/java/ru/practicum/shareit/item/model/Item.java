package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "items")
@NoArgsConstructor
public class Item {

    @Id
    private Long id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_description")
    private String description;

    @OneToOne
    private ItemAvailable available;

    @ManyToOne
    private UserItem user;

    public Item(Long id,
                String name,
                String description,
                ItemAvailable available) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
    }
}
