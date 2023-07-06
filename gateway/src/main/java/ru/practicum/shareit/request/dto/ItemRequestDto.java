package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto {
    @Size(max = 255)
    @NotBlank(groups = Create.class, message = "Поле описание не должно быть пустым")
    private String description;
}