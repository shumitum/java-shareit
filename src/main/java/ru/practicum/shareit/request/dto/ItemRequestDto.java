package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private long id;
    @Size(max = 255)
    @NotBlank(groups = Create.class, message = "Поле описание не должно быть пустым")
    private String description;
    @NotNull
    private LocalDateTime created;
    private List<ItemDto> items;
}