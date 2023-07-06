package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class ItemDto {
    @NotBlank(groups = Create.class, message = "Поле Имя не должно быть пустым")
    @Size(max = 255)
    private String name;
    @NotBlank(groups = Create.class, message = "Поле Описание не должно быть пустым")
    @Size(max = 255)
    private String description;
    @NotNull(groups = Create.class, message = "Поле Доступность к аренде не должно быть пустым")
    private Boolean available;
    private long requestId;
}