package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long requestorId);

    List<ItemRequestDto> getItemRequestsByRequestorId(long requestorId);

    List<ItemRequestDto> getAllItemRequests(long requestorId, long from, long size);

    ItemRequestDto getItemRequestById(long requestId, long userId);
}