package ru.practicum.shareit.item.comment.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CommentCreateDto {
    @NotBlank(message = "Поле Комментарий не должно быть пустым")
    private String text;
}