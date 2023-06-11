package ru.practicum.shareit.item.comment;

import ru.practicum.shareit.item.comment.dto.CommentDto;

public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentDto comment) {
        return Comment.builder()
                .text(comment.getText())
                .build();
    }
}