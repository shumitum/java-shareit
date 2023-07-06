package ru.practicum.shareit.item.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.comment.dto.CommentDto;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);
}