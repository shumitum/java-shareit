package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsParam;

import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_ID_HEADER;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestBody BookingDto bookingDto,
                                    @RequestHeader(USER_ID_HEADER) long bookerId) {
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable long bookingId,
                                     @RequestParam boolean approved,
                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.approveBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable long bookingId,
                                     @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getUserBookingsByState(@RequestParam(defaultValue = "ALL") BookingState state,
                                                   @RequestParam(required = false) Integer from,
                                                   @RequestParam(required = false) Integer size,
                                                   @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBookingByUserIdAndState(GetBookingsParam.of(userId, state, from, size));
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerItemsByState(@RequestParam(defaultValue = "ALL") BookingState state,
                                                 @RequestParam(required = false) Integer from,
                                                 @RequestParam(required = false) Integer size,
                                                 @RequestHeader(USER_ID_HEADER) long ownerId) {
        return bookingService.getOwnerItemsByState(GetBookingsParam.of(ownerId, state, from, size));
    }
}