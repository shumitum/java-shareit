package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class ItemDto {
    private long id;
    @NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Поле Описание не должно быть пустым")
    private String description;
    @NotNull(groups = Create.class, message = "Поле Доступность к аренде не должно быть пустым")
    private Boolean available;
    private BookingInfoDto lastBooking;
    private BookingInfoDto nextBooking;
}