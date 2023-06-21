package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerIdOrderByStartDesc(long userId, Pageable page);

    Page<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Pageable page);

    Page<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    Page<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    Page<Booking> findByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus state, Pageable page);

    Page<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId, Pageable page);

    Page<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end,
                                                                             Pageable page);

    Page<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    Page<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    Page<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status, Pageable page);

    List<Booking> findByItemIdAndStartBeforeAndStatusOrderByStartDesc(long itemId, LocalDateTime time, BookingStatus status);

    List<Booking> findByItemIdAndStartAfterAndStatusOrderByStartAsc(long itemId, LocalDateTime time, BookingStatus status);

    List<Booking> findByBookerIdAndItemIdAndEndBefore(long commentatorId, long itemId, LocalDateTime time);
}