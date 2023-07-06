package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.GetItemParam;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemDto createItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) long ownerId) {
        return itemService.updateItem(itemId, itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestParam(required = false) Integer from,
                                      @RequestParam(required = false) Integer size,
                                      @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getUserItems(GetItemParam.of(userId, from, size));
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam("text") String searchRequest,
                                    @RequestParam(required = false) Integer from,
                                    @RequestParam(required = false) Integer size,
                                    @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.searchItem(searchRequest, GetItemParam.of(userId, from, size));
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable long itemId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        itemService.deleteItemById(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestBody CommentDto comment,
                                    @PathVariable long itemId,
                                    @RequestHeader(USER_ID_HEADER) long commentatorId) {
        return itemService.createComment(comment, itemId, commentatorId);
    }
}