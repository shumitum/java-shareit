package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.exception.InvalidArgumentException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.dto.GetItemParam;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserService userService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    ItemMapper itemMapper;

    @Mock
    BookingMapper bookingMapper;

    @Mock
    CommentMapper commentMapper;


    private Item item;
    private ItemDto itemDto;
    private User user;
    private Comment comment;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(2L)
                .name("name")
                .email("email@email.com")
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .request(null)
                .build();
        comment = Comment.builder()
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
        request = ItemRequest.builder()
                .description("description")
                .requestor(null)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void createItem_whenInvokedWithValidItemDto_thenCreateNewItem() {
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(itemMapper.toItem(any())).thenReturn(item);
        when(itemMapper.toItemDto(any())).thenReturn(itemDto);

        ItemDto actualItem = itemService.createItem(itemDto, anyLong());

        assertEquals(itemDto, actualItem);
        verify(itemRepository, times(1))
                .save(any());
    }

    @Test
    void getItemById_whenInvokedWithCorrectItemId_thenReturnedItem() {
        doNothing().when(userService).checkUserExistence(anyLong());
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(any())).thenReturn(itemDto);
        when(commentRepository.findByItemId(anyLong())).thenReturn(List.of(comment));
        when(commentMapper.toCommentDto(comment)).thenReturn(new CommentDto());

        ItemDto actualItemDto = itemService.getItemById(anyLong(), item.getId());

        assertEquals(actualItemDto, itemDto);
        verify(commentRepository, times(1)).findByItemId(anyLong());
        verify(commentMapper, times(1)).toCommentDto(comment);
    }

    @Test
    void getUserItems_whenInvoked_thenReturnedListOfUserItem() {
        BookingInfoDto bookingInfoDto = new BookingInfoDto(1L, 2L);
        Booking booking = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        GetItemParam params = GetItemParam.of(user.getId(), 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(commentMapper.toCommentDto(comment)).thenReturn(new CommentDto());
        when(itemMapper.toItemDto(any())).thenReturn(itemDto);
        when(commentRepository.findByItemOwnerId(anyLong())).thenReturn(List.of(comment));
        when(itemRepository.findAllByOwnerIdOrderByIdAsc(anyLong(), any()))
                .thenReturn(List.of(item));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toBookingInfoDto(booking)).thenReturn(bookingInfoDto);

        List<ItemDto> actualItems = itemService.getUserItems(params);

        assertEquals(1, actualItems.size());
    }

    @Test
    void searchItem_whenRequestIsBlank_thenReturnedEmptyListOfItems() {
        GetItemParam params = GetItemParam.of(user.getId(), 0, 1);

        List<ItemDto> actualItems = itemService.searchItem("", params);

        assertEquals(0, actualItems.size());
    }

    @Test
    void searchItem_whenRequestIsNotBlank_thenReturnedListOfFitsItems() {
        GetItemParam params = GetItemParam.of(user.getId(), 0, 1);
        doNothing().when(userService).checkUserExistence(anyLong());
        when(itemMapper.toItemDto(any())).thenReturn(itemDto);
        when(itemRepository.findItem("any", params.getPage())).thenReturn(List.of(item));

        List<ItemDto> actualItems = itemService.searchItem("any", params);

        assertEquals(1, actualItems.size());
        verify(itemRepository, times(1)).findItem("any", params.getPage());
    }

    @Test
    void updateItem_whenInvokedWithUpdatedItem_thenUpdateCurrentItem() {
        itemDto.setName("updName");
        itemDto.setDescription("updDescription");
        itemDto.setAvailable(false);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemMapper.toItemDto(any())).thenReturn(itemDto);

        ItemDto actualItemDto = itemService.updateItem(anyLong(), itemDto, user.getId());

        assertEquals(itemDto, actualItemDto);
        assertEquals("updName", actualItemDto.getName());
        assertEquals("updDescription", actualItemDto.getDescription());
        assertEquals(false, actualItemDto.getAvailable());
    }

    @Test
    void updateItem_whenInvokedNotItemOwner_thenThrowException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(IllegalArgumentException.class, () -> itemService.updateItem(item.getId(), itemDto, 999L));
    }


    @Test
    void deleteItemById_whenUserIdANdItemIdIsValid_thenDoesNotThrowException() {
        doNothing().when(userService).checkUserExistence(user.getId());
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> itemService.deleteItemById(item.getId(), user.getId()));
    }

    @Test
    void deleteItemById_whenUserIdANdItemIdIsNotValid_thenThrowIllegalArgumentException() {
        item.setAvailable(false);
        doNothing().when(userService).checkUserExistence(999L);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        assertThrows(IllegalArgumentException.class, () -> itemService.deleteItemById(item.getId(), 999L));
    }

    @Test
    void findItemById_whenInvokedWithCorrectIds_thenReturnItem() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Item actualItem = itemService.findItemById(anyLong());

        assertEquals(item, actualItem);
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void findItemById_whenInvokedWithWrongIds_thenThrowNoSuchElementException() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> itemService.findItemById(anyLong()));
    }

    @Test
    void createComment_whenInvokedWithCorrectParams_thenCreateNewComment() {
        Booking booking = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));
        when(commentMapper.toCommentDto(any())).thenReturn(new CommentDto());

        CommentDto actualCommentDto = itemService.createComment(new CommentDto(), item.getId(), anyLong());

        assertEquals(new CommentDto(), actualCommentDto);
        verify(commentRepository, times(1))
                .save(any());
    }

    @Test
    void createComment_whenInvokedWithValidCommentDto_thenCreateNewComment() {
        Booking booking = Booking.builder()
                .id(3L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .item(item)
                .booker(user)
                .status(BookingStatus.APPROVED)
                .build();
        when(userService.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findByBookerIdAndItemIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(Collections.emptyList());

        assertThrows(InvalidArgumentException.class, () -> itemService.createComment(new CommentDto(),
                item.getId(), anyLong()));
    }

    @Test
    void getLastBookingInfoDto_thenInvokedWithWrongItemId_thenReturnedNull() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdAndStartBeforeAndStatusOrderByStartDesc(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        BookingInfoDto bookingInfo = itemService.getLastBookingInfoDto(anyLong());

        assertNull(bookingInfo);
    }

    @Test
    void getNextBookingInfoDto_thenInvokedWithWrongItemId_thenReturnedNull() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingRepository.findByItemIdAndStartAfterAndStatusOrderByStartAsc(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        BookingInfoDto bookingInfo = itemService.getNextBookingInfoDto(anyLong());

        assertNull(bookingInfo);
    }
}