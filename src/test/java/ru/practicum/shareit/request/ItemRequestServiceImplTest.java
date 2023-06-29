package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.GetItemsRequestParam;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private ItemRequestMapper itemRequestMapper;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private ItemRequest request;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();

        item = Item.builder()
                .id(2L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .request(null)
                .build();
        itemDto = ItemDto.builder()
                .id(2L)
                .name("name")
                .description("description")
                .available(true)
                .build();

        request = ItemRequest.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now().minusHours(2))
                .build();
        requestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.now().minusHours(2))
                .build();
    }

    @Test
    void createItemRequest_whenInvoked_createNewItemRequest() {
        when(itemRequestMapper.toItemRequest(requestDto)).thenReturn(request);
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.save(any())).thenReturn(request);

        ItemRequestDto actualItemRequestDto = itemRequestService.createItemRequest(requestDto, anyLong());

        assertEquals(requestDto, actualItemRequestDto);
        verify(itemRequestRepository, times(1))
                .save(any());
    }

    @Test
    void getItemRequestById_whenInvokedWithCorrectId_thenReturnItemRequest() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);
        when(itemRepository.findByRequestId(anyLong())).thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        ItemRequestDto actualRequestDto = itemRequestService.getItemRequestById(anyLong(), user.getId());

        assertEquals(requestDto, actualRequestDto);
        verify(itemRepository, times(1)).findByRequestId(anyLong());
    }

    @Test
    void getUserItemRequests_whenInvokedWithCorrectUserId_thenReturnedListOfUserItemRequests() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);
        when(itemRequestRepository.findByRequestorIdOrderByCreatedDesc(anyLong()))
                .thenReturn(List.of(request));
        when(itemRepository.findByRequestId(anyLong())).thenReturn(List.of(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        List<ItemRequestDto> requests = itemRequestService.getUserItemRequests(anyLong());

        assertEquals(1, requests.size());
        verify(itemRequestRepository, times(1)).findByRequestorIdOrderByCreatedDesc(anyLong());
        verify(itemRepository, times(1)).findByRequestId(anyLong());
    }

    @Test
    void getOthersItemRequests_whenInvokedWithCorrectUserId_thenReturnedListOfOtherUsersItemRequests() {
        GetItemsRequestParam params = GetItemsRequestParam.of(user.getId(), 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(itemRequestRepository.findItemRequestsByRequestorIdNotOrderByCreatedDesc(anyLong(), any()))
                .thenReturn(List.of(request));
        when(itemRequestMapper.toItemRequestDto(request)).thenReturn(requestDto);
        when(itemRepository.findByRequestId(anyLong())).thenReturn(List.of(item));

        List<ItemRequestDto> requests = itemRequestService.getOthersItemRequests(params);

        assertEquals(1, requests.size());
        verify(itemRequestRepository, times(1))
                .findItemRequestsByRequestorIdNotOrderByCreatedDesc(anyLong(), any());
        verify(itemRepository, times(1)).findByRequestId(anyLong());
    }
}