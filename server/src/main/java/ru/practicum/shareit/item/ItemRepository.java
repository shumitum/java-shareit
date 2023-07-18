package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select * from items where name ilike concat('%', ?1, '%') or description ilike concat('%', ?1, '%')" +
            " AND items.available = true", nativeQuery = true)
    List<Item> findItem(String request, Pageable page);

    List<Item> findAllByOwnerIdOrderByIdAsc(long userId, Pageable page);

    List<Item> findByRequestId(long requestId);
}