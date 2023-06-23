package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.GetItemParam;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto item;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        item = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    @Test
    @SneakyThrows
    void createItem_whenInvokedWithItemIsValid_thenReturnedOkStatusWithItem() {
        when(itemService.createItem(item, 1L)).thenReturn(item);

        String content = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item))
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).createItem(item, 1L);
        assertEquals(objectMapper.writeValueAsString(item), content);
    }

    @Test
    @SneakyThrows
    void updateItem_whenUpdatedItemIsValid_whenUpdateItemAndReturnedIt() {
        long itemId = item.getId();
        long ownerId = 1L;
        when(itemService.updateItem(itemId, item, ownerId)).thenReturn(item);

        String content = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(item))
                        .header(USER_ID_HEADER, ownerId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).updateItem(itemId, item, ownerId);
        assertEquals(objectMapper.writeValueAsString(item), content);
    }

    @Test
    @SneakyThrows
    void getItemById_thenInvokedWithCorrectItemId_thenReturnedOkStatusAndReturnedItem() {
        long itemId = item.getId();
        long userId = 1L;
        when(itemService.getItemById(itemId, userId)).thenReturn(item);

        String content = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).getItemById(itemId, userId);
        assertEquals(objectMapper.writeValueAsString(item), content);
    }

    @Test
    @SneakyThrows
    void getUserItems_whenInvoked_thenReturnedItemsList() {
        long userId = 2L;
        List<ItemDto> items = List.of(item);
        when(itemService.getUserItems(GetItemParam.of(userId, 0, 10)))
                .thenReturn(items);

        String content = mockMvc.perform(get("/items/")
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1))
                .getUserItems(GetItemParam.of(userId, 0, 10));
        assertEquals(objectMapper.writeValueAsString(items), content);
    }

    @Test
    @SneakyThrows
    void searchItem_whenInvoked_thenReturnedSearchedItemsList() {
        String searchRequest = "name";
        long userId = 2L;
        List<ItemDto> items = List.of(item);
        when(itemService.searchItem(searchRequest, GetItemParam.of(userId, 0, 10)))
                .thenReturn(items);

        String content = mockMvc.perform(get("/items/search")
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", searchRequest)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1))
                .searchItem(searchRequest, GetItemParam.of(userId, 0, 10));
        assertEquals(objectMapper.writeValueAsString(items), content);
    }

    @Test
    @SneakyThrows
    void deleteItemById_whenInvoked_thenReturnedOkStatus() {
        long itemId = item.getId();
        long userId = 2L;

        mockMvc.perform(delete("/items/{itemId}", itemId)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk());

        verify(itemService, times(1)).deleteItemById(itemId, userId);
    }

    @Test
    @SneakyThrows
    void createComment_whenInvokedWithValidComment_thenReturnedCreateStatusAndWithComment() {
        long itemId = item.getId();
        long commentatorId = 2L;
        CommentDto comment = CommentDto.builder()
                .id(1L)
                .text("text")
                .build();
        when(itemService.createComment(comment, itemId, commentatorId)).thenReturn(comment);

        String content = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(comment))
                        .header(USER_ID_HEADER, commentatorId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).createComment(comment, itemId, commentatorId);
        assertEquals(objectMapper.writeValueAsString(comment), content);
    }
}