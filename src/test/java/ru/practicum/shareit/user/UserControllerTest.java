package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto user;

    @BeforeEach
    void setUp() {
        user = UserDto.builder()
                .id(1L)
                .name("John")
                .email("john@mail.com")
                .build();
    }

    @Test
    @SneakyThrows
    void createUser_whenUserIsValid_thenReturnedOkStatusWithUser() {
        when(userService.createUser(any())).thenReturn(user);

        String content = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).createUser(any());
        assertEquals(objectMapper.writeValueAsString(user), content);
    }

    @Test
    @SneakyThrows
    void getUserById_whenInvokedWithCorrectId_thenReturnedOkStatusAndUser() {
        when(userService.getUserById(anyLong())).thenReturn(user);

        String content = mockMvc.perform(get("/users/{userId}", anyLong()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).getUserById(anyLong());
        assertEquals(objectMapper.writeValueAsString(user), content);
    }

    @Test
    @SneakyThrows
    void getUserById_whenInvokedWithWrongId_thenReturnedNotFoundStatus() {
        when(userService.getUserById(anyLong())).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/users/{userId}", anyLong()))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(anyLong());
    }

    @Test
    @SneakyThrows
    void getAllUsers_whenInvoked_thenReturnedListOfUsers() {
        List<UserDto> users = List.of(user);
        when(userService.getAllUsers()).thenReturn(users);

        String content = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).getAllUsers();
        assertEquals(objectMapper.writeValueAsString(users), content);
    }

    @Test
    @SneakyThrows
    void updateUser_whenInvoked_thenReturnedUpdatedUser() {
        when(userService.updateUser(user.getId(), user)).thenReturn(user);

        String content = mockMvc.perform(patch("/users/{userId}", user.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, times(1)).updateUser(user.getId(), user);
        assertEquals(objectMapper.writeValueAsString(user), content);
    }

    @Test
    @SneakyThrows
    void deleteUserById_whenInvoked_thenReturnedOkStatus() {
      mockMvc.perform(delete("/users/{userId}", user.getId()))
              .andExpect(status().isOk());

      verify(userService, times(1)).deleteUserById(user.getId());
    }
}