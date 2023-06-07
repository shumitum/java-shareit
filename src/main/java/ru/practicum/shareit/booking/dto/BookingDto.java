package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
@Builder
public class BookingDto {
    private long id;
    @NotBlank(groups = Create.class, message = "Поле Время начала бронирования не должно быть пустым")
    private Timestamp start;
    @NotBlank(groups = Create.class, message = "Поле Время конца бронирования не должно быть пустым")
    private Timestamp end;
    private Item item;
    private User booker;
    private BookingStatus status;
    @NotBlank(groups = Create.class, message = "Поле ID вещи бронирования не должно быть пустым")
    private long itemId;
}