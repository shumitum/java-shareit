package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                            @RequestHeader(USER_ID_HEADER) long requestorId) {
        ItemRequestDto newItemRequestDto = itemRequestService.createItemRequest(itemRequestDto, requestorId);
        log.info("Создан новый запрос на добавление вещи: {}", newItemRequestDto);
        return newItemRequestDto;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getRequestorItemRequests(@RequestHeader(USER_ID_HEADER) long requestorId) {

        log.info("Пользователем с ID={} запрошены собственные заявки на добавление вещей", requestorId);
        return itemRequestService.getItemRequestsByRequestorId(requestorId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getAllItemRequests(@RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam long size,
                                                   @RequestHeader(USER_ID_HEADER) long requestorId) {
        log.info("Пользователем с ID={} запросил список заявок на вещи", requestorId);
        return itemRequestService.getAllItemRequests(requestorId, from, size);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getItemRequestById(@PathVariable long requestId,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошена заявка на вещь с ID={}", userId, requestId);
        return itemRequestService.getItemRequestById(requestId, userId);
    }
}
