package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private long id;
    @FutureOrPresent
    @NotNull(message = "Поле Время начала бронирования не должно быть пустым")
    private LocalDateTime start;
    @Future
    @NotNull(message = "Поле Время конца бронирования не должно быть пустым")
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;
    @NotNull(message = "Поле ID вещи бронирования не должно быть пустым")
    private long itemId;
}