package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByStartDesc(long userId, Pageable page);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Pageable page);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(long userId, BookingStatus state, Pageable page);

    List<Booking> findByItemOwnerIdOrderByStartDesc(long ownerId, Pageable page);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end,
                                                                             Pageable page);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime time, Pageable page);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(long userId, BookingStatus status, Pageable page);

    List<Booking> findByItemIdAndStartBeforeAndStatusOrderByStartDesc(long itemId, LocalDateTime time, BookingStatus status);

    List<Booking> findByItemIdAndStartAfterAndStatusOrderByStartAsc(long itemId, LocalDateTime time, BookingStatus status);

    List<Booking> findByBookerIdAndItemIdAndEndBefore(long commentatorId, long itemId, LocalDateTime time);
}