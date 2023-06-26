package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    @InjectMocks
    BookingMapperImpl bookingMapper;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private UserMapper userMapper;

    private User user;
    private Item item;
    private Booking booking;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
        item = Item.builder()
                .id(2L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .request(null)
                .build();
        booking = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        bookingDto = BookingDto.builder()
                .id(3L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void toBookingDto_whenInvokedWithNotNullBooking_thenReturnedBookingDto() {
        when(itemMapper.toItemDto(item)).thenReturn(new ItemDto());
        when(userMapper.toUserDto(user)).thenReturn(new UserDto());

        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getStatus(), bookingDto.getStatus());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
        assertEquals(booking.getStart(), bookingDto.getStart());
    }

    @Test
    void toBookingDto_whenInvokedWithNullBooking_thenReturnNull() {
        booking = null;

        BookingDto actualBookingDto = bookingMapper.toBookingDto(booking);

        assertNull(actualBookingDto);
    }

    @Test
    void toBooking_whenInvokedWithNotNullBooking_thenReturnedBooking() {
        when(itemMapper.toItem(any())).thenReturn(new Item());
        when(userMapper.toUser(any())).thenReturn(new User());

        Booking actualBooking = bookingMapper.toBooking(bookingDto);

        assertEquals(bookingDto.getId(), actualBooking.getId());
        assertEquals(bookingDto.getStatus(), actualBooking.getStatus());
        assertEquals(bookingDto.getEnd(), actualBooking.getEnd());
        assertEquals(bookingDto.getStart(), actualBooking.getStart());
    }

    @Test
    void toBooking_whenInvokedWithNullBooking_thenReturnNull() {
        bookingDto = null;

        Booking actualBooking = bookingMapper.toBooking(bookingDto);

        assertNull(actualBooking);
    }

    @Test
    void toBookingInfoDto_whenInvokedWithNotNullBooking_thenReturnedBookingInfoDto() {

        BookingInfoDto actualBooking = bookingMapper.toBookingInfoDto(booking);

        assertEquals(booking.getId(), actualBooking.getId());
        assertEquals(booking.getBooker().getId(), actualBooking.getBookerId());
    }

    @Test
    void toBookingInfoDto_whenInvokedWithNullBooking_thenReturnNull() {
        booking = null;

        BookingInfoDto actualBookingInfoDto = bookingMapper.toBookingInfoDto(booking);

        assertNull(actualBookingInfoDto);
    }
}