package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Set;

@Repository
public interface ItemRequestDBRepository extends JpaRepository<ItemRequest, Long> {

    Set<ItemRequest> findAllByAuthorId(Long userId);

    Page<ItemRequest> findAllByAuthorIdIsNot(Long userId, PageRequest pageRequest);

}
