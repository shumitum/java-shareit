package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    @NotBlank(groups = Create.class, message = "Поле Комментарий не должно быть пустым")
    @Size(max = 255)
    private String text;
}