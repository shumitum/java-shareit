package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsParam;

import java.util.List;

public interface BookingService {
    BookingDto createBooking(BookingDto bookingDto, long bookerId);

    BookingDto approveBooking(long bookingId, boolean approved, long userId);

    BookingDto getBookingById(long bookingId, long userId);

    List<BookingDto> getBookingByUserIdAndState(GetBookingsParam params);

    List<BookingDto> getOwnerItemsByState(GetBookingsParam params);

    Booking findBookingById(long bookingId);
}