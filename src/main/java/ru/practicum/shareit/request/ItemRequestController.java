package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.GetItemsRequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.shareit.item.ItemController.USER_ID_HEADER;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemRequestDto createItemRequest(@RequestBody @Validated(Create.class) ItemRequestDto itemRequestDto,
                                            @RequestHeader(USER_ID_HEADER) long requestorId) {
        ItemRequestDto newItemRequestDto = itemRequestService.createItemRequest(itemRequestDto, requestorId);
        log.info("Создан новый запрос на добавление вещи: {}", newItemRequestDto);
        return newItemRequestDto;
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemRequestDto getItemRequestById(@PathVariable long requestId,
                                             @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошен запрос на вещь с ID={}", userId, requestId);
        return itemRequestService.getItemRequestById(requestId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getUserItemRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошены собственные запросы на добавление вещей", userId);
        return itemRequestService.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemRequestDto> getOthersItemRequests(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                      @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                                      @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователь с ID={} запросил список запросов на вещи других пользователей", userId);
        return itemRequestService.getOthersItemRequests(GetItemsRequestParam.of(userId, from, size));
    }
}