package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        Item item = ItemMapper.itemDtoToItem(itemDto);
        item.setOwner(userService.findUserById(userId));
        return ItemMapper.itemToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemMapper.itemToItemDto(findItemById(itemId));
    }

    @Override
    public List<ItemDto> getUserItems(long userId) {
        userService.checkUserExistence(userId);
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper::itemToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItem(String searchRequest, long userId) {
        if (!searchRequest.isBlank()) {
            userService.checkUserExistence(userId);
            //String preparedRequest = searchRequest.toLowerCase().trim();
            return itemRepository.findByNameOrDescriptionContainingIgnoreCase(searchRequest, searchRequest)
                    .stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::itemToItemDto)
                    .collect(Collectors.toList());
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    public ItemDto updateItem(long itemId, ItemDto itemDto, long ownerId) {
        Item updatingItem = findItemById(itemId);
        if (ownerId == updatingItem.getOwner().getId()) {
            if (itemDto.getName() != null) {
                updatingItem.setName(itemDto.getName());
            }
            if (itemDto.getDescription() != null) {
                updatingItem.setDescription(itemDto.getDescription());
            }
            if (itemDto.getAvailable() != null) {
                updatingItem.setAvailable(itemDto.getAvailable());
            }
        } else {
            throw new IllegalArgumentException("Редактировать информацию о вещи может только её владелец");
        }
        return ItemMapper.itemToItemDto(itemRepository.save(updatingItem));
    }

    @Override
    public void deleteItemById(long itemId, long userId) {
        userService.checkUserExistence(userId);
        Item item = findItemById(itemId);
        if (item.getOwner().getId() == userId) {
            if (item.getAvailable()) {
                itemRepository.deleteById(itemId);
            }
        } else {
            throw new IllegalArgumentException("Удалить вещь может только её владелец");
        }
    }

    private Item findItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Вещи с ID=" + itemId + " не существует"));
    }
}