package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class ItemRequestDto {
    @Size(max = 255)
    @NotBlank(groups = Create.class, message = "Поле описание не должно быть пустым")
    private String description;
}