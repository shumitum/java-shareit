package ru.practicum.shareit.booking.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingInfoDto {
    private long id;
    private long bookerId;
}