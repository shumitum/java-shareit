package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    Item createItem(ItemDto itemDto, long userId);

    Item updateItem(long itemId, ItemDto itemDto, long ownerId);

    Item getItemById(long itemId);

    Collection<Item> getUserItems(long userId);

    Collection<Item> searchItem(String searchRequest, long userId);

    void deleteItemById(long itemId, long userId);
}