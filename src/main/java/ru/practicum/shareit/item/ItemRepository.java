package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {
    Item createItem(Item item);

    Item getItemById(long itemId);

    List<Item> getUserItems(long userId);

    List<Item> searchItem(String searchRequest, long userId);

    Item updateItem(long itemId, ItemDto itemDto);

    void deleteItemById(long itemId);
}