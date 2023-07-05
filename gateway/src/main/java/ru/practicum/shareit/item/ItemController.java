package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentClient;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;
    private final CommentClient commentClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestBody @Validated(Create.class) ItemDto itemDto,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        ResponseEntity<Object> newItemDto = itemClient.createItem(itemDto, userId);
        log.info("Создан элемент: {}", newItemDto);
        return newItemDto;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> updateItem(@PathVariable long itemId,
                                             @RequestBody ItemDto itemDto,
                                             @RequestHeader(USER_ID_HEADER) long ownerId) {
        ResponseEntity<Object> updatedItemDto = itemClient.updateItem(itemId, itemDto, ownerId);
        log.info("Данные вещи с ID={} были обновлены: {}", itemId, updatedItemDto);
        return updatedItemDto;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemById(@PathVariable long itemId,
                                              @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошены данные вещи с ID={}", userId, itemId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItems(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                               @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователь с ID={} запросил список своих вещей", userId);
        return itemClient.getUserItems(userId, from, size);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> searchItem(@RequestParam("text") String searchRequest,
                                             @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                             @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователь с ID={} пытался найти: \"{}\"", userId, searchRequest);
        return itemClient.searchItem(searchRequest, userId, from, size);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemById(@PathVariable long itemId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        itemClient.deleteItemById(itemId, userId);
        log.info("Вещь с ID={} удалена владельцем", itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> createComment(@RequestBody @Validated(Create.class) CommentDto comment,
                                                @PathVariable long itemId,
                                                @RequestHeader(USER_ID_HEADER) long commentatorId) {
        ResponseEntity<Object> newComment = commentClient.createComment(comment, itemId, commentatorId);
        log.info("Пользователь с ID={} оставил комментарий вещи с ID={}", commentatorId, itemId);
        return newComment;
    }
}