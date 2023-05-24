package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item createItem(@RequestBody @Valid ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") long userId) {
        Item item = itemService.createItem(itemDto, userId);
        log.info("Создан элемент: {}", item);
        return item;
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@PathVariable long itemId,
                           @RequestBody ItemDto itemDto,
                           @RequestHeader("X-Sharer-User-Id") long ownerId) {
        Item updatedItem = itemService.updateItem(itemId, itemDto, ownerId);
        log.info("Данные вещи с ID={} были обновлены: {}", itemId, updatedItem);
        return updatedItem;
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable long itemId,
                            @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователем с ID={} запрошены данные вещи с ID={}", userId, itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public Collection<Item> getUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователь с ID={} запросил список своих вещей", userId);
        return itemService.getUserItems(userId);
    }

    @GetMapping("/search")
    public Collection<Item> searchItem(@RequestParam("text") String searchRequest,
                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Пользователь с ID={} ищет: \"{}\"", userId, searchRequest);
        return itemService.searchItem(searchRequest, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItemById(@PathVariable long itemId,
                               @RequestHeader("X-Sharer-User-Id") long userId) {
        itemService.deleteItemById(itemId, userId);
        log.info("Вещь с ID={} удалена владельцем", itemId);
    }
}