package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    @Override
    public BookingDto createBooking(BookingDto bookingDto, long userId) {
        return null;
    }
}
