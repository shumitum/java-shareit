package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.GetItemsRequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long requestorId);

    List<ItemRequestDto> getUserItemRequests(long userId);

    List<ItemRequestDto> getOthersItemRequests(GetItemsRequestParam params);

    ItemRequestDto getItemRequestById(long requestId, long userId);
}