package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    private Item item;
    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(2L)
                .name("name")
                .email("email@email.com")
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true).owner(user)
                .request(null)
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true).build();
    }

    @Test
    void toItem_whenInvokedWithNotNullItemDto_thenReturnedItem() {
        Item actualItem = itemMapper.toItem(itemDto);

        assertEquals(itemDto.getId(), actualItem.getId());
        assertEquals(itemDto.getName(), actualItem.getName());
        assertEquals(itemDto.getDescription(), actualItem.getDescription());
        assertEquals(itemDto.getAvailable(), actualItem.getAvailable());
    }

    @Test
    void toItemDto_whenInvokedWithNotNullItem_thenReturnedItemDto() {
        ItemDto actualItemDto = itemMapper.toItemDto(item);

        assertEquals(item.getId(), actualItemDto.getId());
        assertEquals(item.getName(), actualItemDto.getName());
        assertEquals(item.getDescription(), actualItemDto.getDescription());
        assertEquals(item.getAvailable(), actualItemDto.getAvailable());
    }
}