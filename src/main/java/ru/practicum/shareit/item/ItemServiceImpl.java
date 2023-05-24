package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Item createItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);
        Item item = ItemMapper.toItem(itemDto);
        item.setOwnerId(userId);
        return itemRepository.createItem(item);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public Collection<Item> getUserItems(long userId) {
        userRepository.getUserById(userId);
        return itemRepository.getUserItems(userId);
    }

    @Override
    public Collection<Item> searchItem(String searchRequest, long userId) {
        userRepository.getUserById(userId);
        String normalizeSearchRequest = searchRequest.toLowerCase().trim();
        return itemRepository.searchItem(normalizeSearchRequest, userId);
    }

    @Override
    public Item updateItem(long itemId, ItemDto itemDto, long ownerId) {
        Item updatingItem = itemRepository.getItemById(itemId);
        if (ownerId == updatingItem.getOwnerId()) {
            itemRepository.updateItem(itemId, itemDto);
        } else {
            throw new IllegalArgumentException("Редактировать информацию о вещи может только её владелец");
        }
        return itemRepository.getItemById(itemId);
    }

    @Override
    public void deleteItemById(long itemId, long userId) {
        userRepository.getUserById(userId);
        Item item = itemRepository.getItemById(itemId);
        if (item.getOwnerId() == userId & item.getAvailable()) {
            itemRepository.deleteItemById(itemId);
        } else {
            throw new IllegalArgumentException("Удалить вещь может только её владелец");
        }
    }
}