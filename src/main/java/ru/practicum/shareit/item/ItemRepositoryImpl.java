package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private long itemId;

    @Override
    public Item createItem(Item item) {
        item.setId(++itemId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new NoSuchElementException("Вещи с ID=" + itemId + " не существует");
        }
    }

    @Override
    public Collection<Item> getUserItems(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> searchItem(String searchRequest, long userId) {
        List<Item> searchResult = new LinkedList<>();
        if (!searchRequest.isBlank()) {
            for (Item item : items.values().stream().filter(Item::getAvailable).collect(Collectors.toList())) {
                if (item.getName().toLowerCase().contains(searchRequest) |
                        item.getDescription().toLowerCase().contains(searchRequest)) {
                    searchResult.add(item);
                }
            }
        }
        return searchResult;
    }

    @Override
    public Item updateItem(long itemId, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            getItemById(itemId).setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            getItemById(itemId).setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            getItemById(itemId).setAvailable(itemDto.getAvailable());
        }
        return getItemById(itemId);
    }

    @Override
    public void deleteItemById(long itemId) {
        items.remove(itemId);
    }
}