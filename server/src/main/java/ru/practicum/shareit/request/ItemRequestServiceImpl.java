package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.GetItemsRequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final ItemRequestMapper itemRequestMapper;

    @Transactional
    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long requestorId) {
        final ItemRequest newItemRequest = itemRequestMapper.toItemRequest(itemRequestDto);
        newItemRequest.setCreated(LocalDateTime.now());
        newItemRequest.setRequestor(userService.findUserById(requestorId));
        return itemRequestMapper.toItemRequestDto(itemRequestRepository.save(newItemRequest));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemRequestDto getItemRequestById(long requestId, long userId) {
        userService.checkUserExistence(userId);
        final ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException(String.format("Заявки с ID=%d не существует", requestId)));
        final ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);
        final List<ItemDto> items = itemRepository.findByRequestId(requestId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
        itemRequestDto.setItems(items);
        return itemRequestDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getUserItemRequests(long userId) {
        userService.checkUserExistence(userId);
        return itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId).stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(itemRequest -> itemRequest.setItems(getItemDtoList(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemRequestDto> getOthersItemRequests(GetItemsRequestParam params) {
        final long userId = params.getUserId();
        userService.checkUserExistence(userId);
        return itemRequestRepository.findItemRequestsByRequestorIdNotOrderByCreatedDesc(userId, params.getPage())
                .stream()
                .map(itemRequestMapper::toItemRequestDto)
                .peek(itemRequest -> itemRequest.setItems(getItemDtoList(itemRequest.getId())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemDto> getItemDtoList(long itemRequestId) {
        return itemRepository.findByRequestId(itemRequestId)
                .stream()
                .map(itemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}