package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Set;

@Repository
public interface DBItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByNameContainingOrDescriptionContainingIgnoreCase(String text1,
                                                                     String text2,
                                                                     Pageable pageable);

    List<Item> findByOwnerId(Long id);

    List<Item> findItemsByOwnerId(Long id, Pageable pageable);
    Set<Item> findItemsByRequestId(Long id);
}
