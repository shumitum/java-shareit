package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.validationgroup.Create;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.item.ItemController.USER_ID_HEADER;

@Controller
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemRequest(@RequestBody @Validated(Create.class) ItemRequestDto itemRequestDto,
                                                    @RequestHeader(USER_ID_HEADER) long requestorId) {
        ResponseEntity<Object> newItemRequestDto = itemRequestClient.createItemRequest(itemRequestDto, requestorId);
        log.info("Создан новый запрос на добавление вещи: {}", newItemRequestDto);
        return newItemRequestDto;
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getItemRequestById(@PathVariable long requestId,
                                                     @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошен запрос на вещь с ID={}", userId, requestId);
        return itemRequestClient.getItemRequestById(requestId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getUserItemRequests(@RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователем с ID={} запрошены собственные запросы на добавление вещей", userId);
        return itemRequestClient.getUserItemRequests(userId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getOthersItemRequests(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                        @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
                                                        @RequestHeader(USER_ID_HEADER) long userId) {
        log.info("Пользователь с ID={} запросил список запросов на вещи других пользователей", userId);
        return itemRequestClient.getOthersItemRequests(userId, from, size);
    }
}