package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
public class BookingDto {
    @FutureOrPresent
    @NotNull(message = "Поле Время начала бронирования не должно быть пустым")
    private LocalDateTime start;
    @Future
    @NotNull(message = "Поле Время конца бронирования не должно быть пустым")
    private LocalDateTime end;
    @NotNull(message = "Поле ID вещи бронирования не должно быть пустым")
    private long itemId;
}