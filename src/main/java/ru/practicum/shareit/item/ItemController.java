package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.validationgroup.Create;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestBody @Validated(Create.class) ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        ItemDto newItemDto = itemService.createItem(itemDto, userId);
        log.info("Создан элемент: {}", newItemDto);
        return newItemDto;
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long ownerId) {
        ItemDto updatedItemDto = itemService.updateItem(itemId, itemDto, ownerId);
        log.info("Данные вещи с ID={} были обновлены: {}", itemId, updatedItemDto);
        return updatedItemDto;
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable long itemId,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователем с ID={} запрошены данные вещи с ID={}", userId, itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователь с ID={} запросил список своих вещей", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItem(@RequestParam("text") String searchRequest,
                                    @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователь с ID={} пытался найти: \"{}\"", userId, searchRequest);
        return itemService.searchItem(searchRequest, userId);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteItemById(@PathVariable long itemId,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteItemById(itemId, userId);
        log.info("Вещь с ID={} удалена владельцем", itemId);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(@RequestBody @Validated(Create.class) CommentDto comment,
                                    @PathVariable long itemId,
                                    @RequestHeader("X-Sharer-User-Id") long commentatorId) {
        CommentDto newComment = itemService.createComment(comment, itemId, commentatorId);
        log.info("Пользователь с ID={} оставил комментарий вещи с ID={}", commentatorId, itemId);
        return newComment;
    }
}