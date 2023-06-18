package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, long requestorId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getItemRequestsByRequestorId(long requestorId) {
        return null;
    }

    @Override
    public List<ItemRequestDto> getAllItemRequests(long requestorId, long from, long size) {
        return null;
    }

    @Override
    public ItemRequestDto getItemRequestById(long requestId, long userId) {
        return null;
    }
}