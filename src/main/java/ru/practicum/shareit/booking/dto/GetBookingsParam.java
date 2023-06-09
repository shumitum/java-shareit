package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.exception.InvalidArgumentException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBookingsParam {
    private long userId;
    private String state;
    private Integer from;
    private Integer size;
    private PageRequest page;

    public static GetBookingsParam of(long userId, String state, Integer from, Integer size) {
        return GetBookingsParam.builder()
                .userId(userId)
                .state(state)
                .from(from)
                .size(size)
                .build();
    }

    public BookingState getBookingState() {
        BookingState newState;
        try {
            newState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new InvalidArgumentException("Unknown state: UNSUPPORTED_STATUS");
        }
        return newState;
    }

    public PageRequest getPage() {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}