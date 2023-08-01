package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users_items")
@NoArgsConstructor
public class UserItem {

    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "item_id")
    private Long itemId;

    public UserItem(Long userId, Long itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }
}
