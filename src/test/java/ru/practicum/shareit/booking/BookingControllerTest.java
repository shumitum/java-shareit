package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.GetBookingsParam;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto booking;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        booking = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusHours(1L))
                .end(LocalDateTime.now().plusHours(2L))
                .itemId(2L)
                .build();
    }

    @Test
    @SneakyThrows
    void createBooking_whenBookingIsValid_thenReturnedOkStatusWithBooking() {
        when(bookingService.createBooking(booking, 1L)).thenReturn(booking);

        String content = mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(booking))
                        .header(USER_ID_HEADER, 1L))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).createBooking(booking, 1L);
        assertEquals(objectMapper.writeValueAsString(booking), content);
    }

    @Test
    @SneakyThrows
    void approveBooking_whenApprovedIsTrue_thenApproveBookingAndReturnUpdatedBooking() {
        booking.setStatus(BookingStatus.APPROVED);
        long bookingId = booking.getId();
        long userId = 1L;
        when(bookingService.approveBooking(bookingId, true, userId)).thenReturn(booking);

        String content = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(booking))
                        .header(USER_ID_HEADER, userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).approveBooking(bookingId, true, userId);
        assertEquals(objectMapper.writeValueAsString(booking), content);
    }

    @Test
    @SneakyThrows
    void getBookingById_whenInvokedWithCorrectId_thenReturnedOkStatusAndBooking() {
        long bookingId = booking.getId();
        long userId = 2L;
        when(bookingService.getBookingById(bookingId, userId)).thenReturn(booking);

        String content = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).getBookingById(bookingId, userId);
        assertEquals(objectMapper.writeValueAsString(booking), content);
    }

    @Test
    @SneakyThrows
    void getUserBookingsByState() {
        long userId = 2L;
        List<BookingDto> bookings = List.of(booking);
        when(bookingService.getBookingByUserIdAndState(GetBookingsParam.of(userId, "ALL", 0, 10)))
                .thenReturn(bookings);

        String content = mockMvc.perform(get("/bookings/")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_ID_HEADER, userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1))
                .getBookingByUserIdAndState(GetBookingsParam.of(userId, "ALL", 0, 10));
        assertEquals(objectMapper.writeValueAsString(bookings), content);
    }

    @Test
    @SneakyThrows
    void getOwnerItemsByState() {
        long ownerId = 2L;
        String state = "ALL";
        List<BookingDto> bookings = List.of(booking);
        when(bookingService.getOwnerItemsByState(GetBookingsParam.of(ownerId, state, 0, 10)))
                .thenReturn(bookings);

        String content = mockMvc.perform(get("/bookings/owner")
                        .param("state", state)
                        .param("from", "0")
                        .param("size", "10")
                        .header(USER_ID_HEADER, ownerId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1))
                .getOwnerItemsByState(GetBookingsParam.of(ownerId, state, 0, 10));
        assertEquals(objectMapper.writeValueAsString(bookings), content);
    }
}