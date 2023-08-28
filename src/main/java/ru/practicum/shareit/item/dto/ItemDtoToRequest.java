package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDtoToRequest {

    private Long id;

    private String name;

    private String description;

    private Long requestId;

    private Boolean available;
}
