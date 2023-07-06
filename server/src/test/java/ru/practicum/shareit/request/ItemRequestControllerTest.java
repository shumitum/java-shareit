package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.GetItemsRequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequest;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        itemRequest = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .build();
    }

    @Test
    @SneakyThrows
    void createItemRequest_whenInvokedWithItemRequestIsValid_thenReturnedOkStatusWithItemRequest() {
        long requestorId = 1L;
        when(itemRequestService.createItemRequest(itemRequest, requestorId)).thenReturn(itemRequest);

        String content = mockMvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequest))
                        .header(USER_ID_HEADER, requestorId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).createItemRequest(itemRequest, requestorId);
        assertEquals(objectMapper.writeValueAsString(itemRequest), content);
    }

    @Test
    @SneakyThrows
    void getItemRequestById_thenInvokedWithCorrectItemRequestId_thenReturnedOkStatusAndReturnedItemRequest() {
        long itemRequestId = itemRequest.getId();
        long userId = 1L;
        when(itemRequestService.getItemRequestById(itemRequestId, userId)).thenReturn(itemRequest);

        String content = mockMvc.perform(get("/requests/{requestId}", itemRequestId)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).getItemRequestById(itemRequestId, userId);
        assertEquals(objectMapper.writeValueAsString(itemRequest), content);
    }

    @Test
    @SneakyThrows
    void getUserItemRequests_whenInvokedWithCorrectUserId_thenReturnedOkStatusAndReturnedItemRequestList() {
        long userId = 2L;
        List<ItemRequestDto> itemRequests = List.of(itemRequest);
        when(itemRequestService.getUserItemRequests(userId)).thenReturn(itemRequests);

        String content = mockMvc.perform(get("/requests/")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).getUserItemRequests(userId);
        assertEquals(objectMapper.writeValueAsString(itemRequests), content);
    }

    @Test
    @SneakyThrows
    void getOthersItemRequests_whenInvokedWithCorrectUserId_thenReturnedOkStatusAndReturnedItemRequestList() {
        long userId = 2L;
        List<ItemRequestDto> itemRequests = List.of(itemRequest);
        when(itemRequestService.getOthersItemRequests(GetItemsRequestParam.of(userId, 0, 10)))
                .thenReturn(itemRequests);

        String content = mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, userId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1))
                .getOthersItemRequests(GetItemsRequestParam.of(userId, 0, 10));
        assertEquals(objectMapper.writeValueAsString(itemRequests), content);
    }
}