package ru.practicum.shareit.booking.dto;


import ru.practicum.shareit.exception.InvalidArgumentException;

public enum BookingState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING;

    public static BookingState of(String stringState) {
        BookingState newState;
        try {
            newState = BookingState.valueOf(stringState.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException(String.format("Unknown state: %s", stringState));
        }
        return newState;
    }
}
