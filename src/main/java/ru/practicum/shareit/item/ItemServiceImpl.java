package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        userRepository.getUserById(userId);
        Item item = ItemMapper.itemDtoToItem(itemDto);
        item.setOwnerId(userId);
        return ItemMapper.itemToItemDto(itemRepository.createItem(item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemMapper.itemToItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        userRepository.getUserById(userId);
        return itemRepository.getUserItems(userId)
                .stream()
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String searchRequest, long userId) {
        userRepository.getUserById(userId);
        String preparedRequest = searchRequest.toLowerCase().trim();
        return itemRepository.searchItem(preparedRequest, userId)
                .stream()
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long ownerId) {
        Item updatingItem = itemRepository.getItemById(itemId);
        if (ownerId == updatingItem.getOwnerId()) {
            itemRepository.updateItem(itemId, itemDto);
        } else {
            throw new IllegalArgumentException("Редактировать информацию о вещи может только её владелец");
        }
        return ItemMapper.itemToItemDto(itemRepository.getItemById(itemId));
    }

    @Override
    public void deleteItemById(long itemId, long userId) {
        userRepository.getUserById(userId);
        Item item = itemRepository.getItemById(itemId);
        if (item.getOwnerId() == userId) {
            if (item.getAvailable()) {
                itemRepository.deleteItemById(itemId);
            }
        } else {
            throw new IllegalArgumentException("Удалить вещь может только её владелец");
        }
    }
}