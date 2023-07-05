package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemRequestMapperTest {

    private final ItemRequestMapper itemRequestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Test
    void toItemRequestDto_whenInvokedWithNotNullItemRequest_thenReturnedItemRequestDto() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now().minusHours(2))
                .build();

        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);

        assertEquals(itemRequest.getId(), itemRequestDto.getId());
        assertEquals(itemRequest.getDescription(), itemRequestDto.getDescription());
        assertEquals(itemRequest.getCreated(), itemRequestDto.getCreated());
    }

    @Test
    void toItemRequestDto_whenInvokedWittNullItemRequest_thenReturnedNull() {
        ItemRequest itemRequest = null;

        ItemRequestDto itemRequestDto = itemRequestMapper.toItemRequestDto(itemRequest);

        assertNull(itemRequestDto);
    }

    @Test
    void toItemRequest_whenInvokedWithNotNullItemRequestDto_thenReturnedItemRequest() {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now().minusHours(2))
                .build();

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        assertEquals(itemRequestDto.getId(), itemRequest.getId());
        assertEquals(itemRequestDto.getDescription(), itemRequest.getDescription());
        assertEquals(itemRequestDto.getCreated(), itemRequest.getCreated());
    }

    @Test
    void toItemRequest_whenInvokedWittNullItemRequest_thenReturnedNull() {
        ItemRequestDto itemRequestDto = null;

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto);

        assertNull(itemRequest);
    }
}