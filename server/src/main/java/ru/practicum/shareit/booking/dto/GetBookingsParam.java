package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingState;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetBookingsParam {
    private long userId;
    private BookingState state;
    private Integer from;
    private Integer size;
    private PageRequest page;

    public static GetBookingsParam of(long userId, BookingState state, Integer from, Integer size) {
        return GetBookingsParam.builder()
                .userId(userId)
                .state(state)
                .from(from)
                .size(size)
                .build();
    }

    public PageRequest getPage() {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}