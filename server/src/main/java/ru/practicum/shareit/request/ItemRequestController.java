package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.GetItemsRequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_ID_HEADER;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                            @RequestHeader(USER_ID_HEADER) long requestorId) {
        return itemRequestService.createItemRequest(itemRequestDto, requestorId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@PathVariable long requestId,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getUserItemRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestService.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getOthersItemRequests(@RequestParam(required = false) Integer from,
                                                      @RequestParam(required = false) Integer size,
                                                      @RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestService.getOthersItemRequests(GetItemsRequestParam.of(userId, from, size));
    }
}