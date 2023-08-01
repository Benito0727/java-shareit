package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "item_available")
@NoArgsConstructor
public class ItemAvailable {

    @Id
    private Long itemId;

    @Column(name = "available")
    private Boolean status;

    public ItemAvailable(Long itemId, Boolean status) {
        this.itemId = itemId;
        this.status = status;
    }
}
