package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager tem;
    private User user;
    private Item item;
    private Booking booking;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .request(null)
                .build();
        booking = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        tem.persist(user);
        tem.persist(item);
        tem.persist(booking);
    }

    @AfterEach
    void tearDown() {
        user = null;
        item = null;
        booking = null;
    }

    @Test
    void findByBookerIdOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(user.getId(), Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.APPROVED, bookings.get(0).getStatus());
    }

    @Test
    void findByBookerIdOrderByStartDesc_whenInvoked_thenReturnPageOne() {
        Booking newBooking = Booking.builder()
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        tem.persist(newBooking);


        List<Booking> bookings = bookingRepository.findByBookerIdOrderByStartDesc(user.getId(), PageRequest.of(0, 1));

        assertEquals(1, bookings.size());
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStart(LocalDateTime.now().minusHours(1));

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_whenInvoked_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Pageable.unpaged());

        assertEquals(0, bookings.size());
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStart(LocalDateTime.now().minusHours(2));
        booking.setEnd(LocalDateTime.now().minusHours(1));

        List<Booking> bookings = bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(user.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(user.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStatus(BookingStatus.WAITING);

        List<Booking> bookings = bookingRepository.findByBookerIdAndStatusOrderByStartDesc(user.getId(),
                BookingStatus.WAITING,
                Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdOrderByStartDesc(user.getId(), Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.APPROVED, bookings.get(0).getStatus());
    }

    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStart(LocalDateTime.now().minusHours(1));

        List<Booking> bookings = bookingRepository
                .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(user.getId(),
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findByItemOwnerIdAndEndBeforeOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStart(LocalDateTime.now().minusHours(2));
        booking.setEnd(LocalDateTime.now().minusHours(1));

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(user.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());

    }

    @Test
    void findByItemOwnerIdAndStartAfterOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(user.getId(),
                LocalDateTime.now(),
                Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
    }

    @Test
    void findByItemOwnerIdAndStatusOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStatus(BookingStatus.WAITING);

        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(user.getId(),
                BookingStatus.WAITING,
                Pageable.unpaged());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.WAITING, bookings.get(0).getStatus());
    }

    @Test
    void findByItemIdAndStartBeforeAndStatusOrderByStartDesc_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStart(LocalDateTime.now().minusHours(2));

        List<Booking> bookings = bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(),
                LocalDateTime.now(), BookingStatus.APPROVED);

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.APPROVED, bookings.get(0).getStatus());
    }

    @Test
    void findByItemIdAndStartBeforeAndStatusOrderByStartDesc_whenInvoked_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByStartDesc(item.getId(),
                LocalDateTime.now(), BookingStatus.APPROVED);

        assertEquals(0, bookings.size());
    }

    @Test
    void findByItemIdAndStartAfterAndStatusOrderByStartAsc_whenInvoked_thenReturnListOfOneBooking() {
        List<Booking> bookings = bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(item.getId(),
                LocalDateTime.now(), BookingStatus.APPROVED);

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.APPROVED, bookings.get(0).getStatus());
    }

    @Test
    void findByBookerIdAndItemIdAndEndBefore_whenInvoked_thenReturnEmptyList() {
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemIdAndEndBefore(user.getId(),
                item.getId(), LocalDateTime.now());

        assertEquals(0, bookings.size());
    }

    @Test
    void findByBookerIdAndItemIdAndEndBefore_whenInvoked_thenReturnListOfOneBooking() {
        booking.setStart(LocalDateTime.now().minusHours(2));
        booking.setEnd(LocalDateTime.now().minusHours(1));

        List<Booking> bookings = bookingRepository.findByBookerIdAndItemIdAndEndBefore(user.getId(),
                item.getId(), LocalDateTime.now());

        assertEquals(1, bookings.size());
        assertEquals(booking.getId(), bookings.get(0).getId());
        assertEquals(BookingStatus.APPROVED, bookings.get(0).getStatus());
    }
}