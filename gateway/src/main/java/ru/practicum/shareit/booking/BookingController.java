package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.ItemController.USER_ID_HEADER;


@Controller
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestBody @Valid BookingDto bookingDto,
                                                @RequestHeader(USER_ID_HEADER) long bookerId) {
        ResponseEntity<Object> newBookingDto = bookingClient.createBooking(bookingDto, bookerId);
        log.info("Создан новый запрос на бронирование: {}", newBookingDto);
        return newBookingDto;
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> approveBooking(@PathVariable long bookingId,
                                                 @RequestParam boolean approved,
                                                 @RequestHeader(USER_ID_HEADER) long userId) {
        ResponseEntity<Object> bookingDto = bookingClient.approveBooking(bookingId, approved, userId);
        log.info("Статус заявки на бронирование вещи с ID={} изменен на: {}", bookingId, approved);
        return bookingDto;
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getBookingById(@PathVariable long bookingId,
                                                 @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошена заявка на бронирование с ID={}", userId, bookingId);
        return bookingClient.getBookingById(bookingId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserBookingsByState(@RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                         @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                         @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                         @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошены заявки на бронирование со статусом: {}", userId, stateParam);
        return bookingClient.getBookingByUserIdAndState(userId, BookingState.of(stateParam), from, size);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOwnerItemsByState(@RequestParam(name = "state", defaultValue = "all") String stateParam,
                                                       @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                       @RequestParam(name = "size", defaultValue = "10") @Positive Integer size,
                                                       @RequestHeader(USER_ID_HEADER) long ownerId) {
        log.info("Пользователем с ID={} запросил список своих вещей со статусом: {}", ownerId, stateParam);
        return bookingClient.getOwnerItemsByState(ownerId, BookingState.of(stateParam), from, size);
    }
}