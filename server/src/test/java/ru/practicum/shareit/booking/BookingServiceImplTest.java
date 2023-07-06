package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsParam;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemService itemService;

    @Mock
    private BookingMapper bookingMapper;

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

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
                .item(itemMapper.toItemDto(item))
                .booker(userMapper.toUserDto(user))
                .status(BookingStatus.APPROVED)
                .build();
    }

    @Test
    void createBooking_whenInvokedWithCorrectBookingDto_thenCreateNewBooking() {
        when(bookingRepository.save(any())).thenReturn(booking);
        when(itemService.findItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(bookingMapper.toBooking(any())).thenReturn(booking);
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        BookingDto actualBooking = bookingService.createBooking(bookingDto, anyLong());

        assertEquals(bookingDto, actualBooking);
    }

    @Test
    void createBooking_whenInvokedWithItemNotAvailable_thenThrowInvalidArgumentException() {
        item.setAvailable(false);
        when(itemService.findItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(user);

        assertThrows(InvalidArgumentException.class, () -> bookingService.createBooking(bookingDto, anyLong()));
    }

    @Test
    void createBooking_whenBookingEndTimeIsBeforeStart_thenThrowInvalidArgumentException() {
        bookingDto.setEnd(LocalDateTime.now().minusDays(10));
        when(itemService.findItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(user);

        assertThrows(InvalidArgumentException.class, () -> bookingService.createBooking(bookingDto, anyLong()));
    }

    @Test
    void createBooking_whenBookingEndAndStartIsTheSame_thenThrowInvalidArgumentException() {
        LocalDateTime now = LocalDateTime.now();
        bookingDto.setEnd(now);
        bookingDto.setStart(now);
        when(itemService.findItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(anyLong())).thenReturn(user);

        assertThrows(InvalidArgumentException.class, () -> bookingService.createBooking(bookingDto, anyLong()));
    }

    @Test
    void createBooking_whenBookerIdAndItemOwnerIdEquals_thenThrowNoSuchElementException() {
        when(itemService.findItemById(anyLong())).thenReturn(item);
        when(userService.findUserById(user.getId())).thenReturn(user);

        assertThrows(NoSuchElementException.class, () -> bookingService.createBooking(bookingDto, user.getId()));
    }

    @Test
    void approveBooking_whenInvokedWithApprovesTrue_thenSetBookingStatusApproved() {
        booking.setStatus(BookingStatus.WAITING);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        BookingDto actualBooking = bookingService.approveBooking(anyLong(), true, user.getId());

        assertEquals(bookingDto, actualBooking);
        assertEquals(BookingStatus.APPROVED, actualBooking.getStatus());
    }

    @Test
    void approveBooking_whenInvokedWithApprovesFalse_thenRejectBooking() {
        booking.setStatus(BookingStatus.WAITING);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        BookingDto actualBooking = bookingService.approveBooking(anyLong(), false, user.getId());

        assertEquals(BookingStatus.REJECTED, booking.getStatus());
    }

    @Test
    void approveBooking_whenInvokedAlreadyApprovedBooking_thenThrowInvalidArgumentException() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(InvalidArgumentException.class, () -> bookingService.approveBooking(anyLong(), true, user.getId()));
    }

    @Test
    void approveBooking_whenInvokedWithUserNotOwner_thenThrowNoSuchElementException() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NoSuchElementException.class, () -> bookingService.approveBooking(anyLong(), true, 999L));
    }

    @Test
    void getBookingById_whenInvokedWithCorrectBookingId_thenReturnedBooking() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        BookingDto actualBooking = bookingService.getBookingById(anyLong(), user.getId());

        assertEquals(bookingDto, actualBooking);
    }

    @Test
    void getBookingById_whenInvokedWithWrongBookingId_thenThrowNoSuchElementException() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NoSuchElementException.class, () -> bookingService.getBookingById(anyLong(), 999L));
    }

    @Test
    void getBookingByUserIdAndState_whenInvokedWithStateAll_thenReturnedBookingsWithStateAll() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.ALL, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByBookerIdOrderByStartDesc(user.getId(), params.getPage()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getBookingByUserIdAndState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
    }

    @Test
    void getBookingByUserIdAndState_whenInvokedWithStateCurrent_thenReturnedBookingsWithStateCurrent() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.CURRENT, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any())).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getBookingByUserIdAndState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any());
    }

    @Test
    void getBookingByUserIdAndState_whenInvokedWithStatePast_thenReturnedBookingsWithStatePast() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.PAST, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getBookingByUserIdAndState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingByUserIdAndState_whenInvokedWithStateFuture_thenReturnedBookingsWithStateFuture() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.FUTURE, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getBookingByUserIdAndState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByBookerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingByUserIdAndState_whenInvokedWithStatWaiting_thenReturnedBookingsWithStateWaiting() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.WAITING, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getBookingByUserIdAndState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getBookingByUserIdAndState_whenInvokedWithStateRejected_thenReturnedBookingsWithStateRejected() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.REJECTED, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getBookingByUserIdAndState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByBookerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerItemsByState_whenInvokedWithStateAll_thenReturnedBookingsWithStateAll() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.ALL, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getOwnerItemsByState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1)).findByItemOwnerIdOrderByStartDesc(anyLong(), any());
    }

    @Test
    void getOwnerItemsByState_whenInvokedWithStateCurrent_thenReturnedBookingsWithStateCurrent() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.CURRENT, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class), any(LocalDateTime.class), any())).thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getOwnerItemsByState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class), any(LocalDateTime.class), any());
    }

    @Test
    void getOwnerItemsByState_whenInvokedWithStatePast_thenReturnedBookingsWithStatePast() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.PAST, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getOwnerItemsByState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerItemsByState_whenInvokedWithStateFuture_thenReturnedBookingsWithStateFuture() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.FUTURE, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getOwnerItemsByState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerItemsByState_whenInvokedWithStateWaiting_thenReturnedBookingsWithStateWaiting() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.WAITING, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getOwnerItemsByState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void getOwnerItemsByState_whenInvokedWithStateRejected_thenReturnedBookingsWithStateRejected() {
        GetBookingsParam params = GetBookingsParam.of(user.getId(), BookingState.REJECTED, 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingDto(any())).thenReturn(bookingDto);

        List<BookingDto> bookings = bookingService.getOwnerItemsByState(params);

        assertEquals(1, bookings.size());
        assertEquals(3L, bookings.get(0).getId());
        verify(bookingRepository, times(1))
                .findByItemOwnerIdAndStatusOrderByStartDesc(anyLong(), any(), any());
    }

    @Test
    void findBookingById_whenInvokedWithInvalidBookingId_thenThrowNoSuchElementException() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookingService.findBookingById(anyLong()));
    }
}