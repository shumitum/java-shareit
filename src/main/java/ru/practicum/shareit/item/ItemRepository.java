package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item createItem(Item item);

    Item getItemById(long itemId);

    Collection<Item> getUserItems(long userId);

    Collection<Item> searchItem(String searchRequest, long userId);

    Item updateItem(long itemId, ItemDto itemDto);

    void deleteItemById(long itemId);
}