package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface DBItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingOrDescriptionContainingIgnoreCase(String text1, String text2);

    List<Item> findByOwner(Long id);
}
