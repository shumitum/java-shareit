package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
public class CommentDto {
    @NotBlank(groups = Create.class, message = "Поле Комментарий не должно быть пустым")
    @Size(max = 255)
    private String text;
}