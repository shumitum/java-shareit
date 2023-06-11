package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    private final BookingRepository bookingRepository;

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(userService.findUserById(userId));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItemById(long itemId, long userId) {
        userService.checkUserExistence(userId);
        Item item = findItemById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (userId == item.getOwner().getId()) {
            itemDto.setLastBooking(getLastBookingInfoDto(itemId));
            itemDto.setNextBooking(getNextBookingInfoDto(itemId));
        }
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getUserItems(long userId) {
        userService.checkUserExistence(userId);
        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .peek(item -> item.setLastBooking(getLastBookingInfoDto(item.getId())))
                .peek(item -> item.setNextBooking(getNextBookingInfoDto(item.getId())))
                .sorted(Comparator.comparingLong(ItemDto::getId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> searchItem(String searchRequest, long userId) {
        if (!searchRequest.isBlank()) {
            userService.checkUserExistence(userId);
            return itemRepository.findItem(searchRequest.trim())
                    .stream()
                    .filter(Item::getAvailable)
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        } else {
            return new LinkedList<>();
        }
    }

    @Transactional
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
        return ItemMapper.toItemDto(itemRepository.save(updatingItem));
    }

    @Transactional
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

    @Transactional(readOnly = true)
    @Override
    public Item findItemById(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("Вещи с ID=" + itemId + " не существует"));
    }

    private BookingInfoDto getLastBookingInfoDto(long itemId) {
        findItemById(itemId);
        List<Booking> bookings = bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByStartDesc(itemId,
                LocalDateTime.now(), BookingStatus.APPROVED);
        return !bookings.isEmpty() ? BookingMapper.toBookingInfoDto(bookings.get(0)) : null;
    }

    private BookingInfoDto getNextBookingInfoDto(long itemId) {
        findItemById(itemId);
        List<Booking> bookings = bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(itemId,
                LocalDateTime.now(), BookingStatus.APPROVED);
        return !bookings.isEmpty() ? BookingMapper.toBookingInfoDto(bookings.get(0)) : null;
    }
}