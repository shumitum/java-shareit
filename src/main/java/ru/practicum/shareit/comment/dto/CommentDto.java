package ru.practicum.shareit.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.User;

import java.sql.Timestamp;

@Data
@Builder
public class CommentDto {
    private long id;
    private String text;
    private User authorName;
    private Timestamp created;
}
