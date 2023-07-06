package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
        userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();
    }

    @Test
    void createUser_whenInvokedWithValidUserDto_thenCreateNewUser() {
        when(userRepository.save(any())).thenReturn(user);
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        UserDto actualUserDto = userService.createUser(userDto);

        assertEquals(userDto, actualUserDto);
    }

    @Test
    void getUserById_whenInvokedWithCorrectUserId_thenReturnedUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        UserDto actualUserDto = userService.getUserById(anyLong());

        assertEquals(userDto, actualUserDto);
    }

    @Test
    void getUserById_whenInvokedWithWrongUserId_thenThrowNoSuchElementException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(anyLong()));
    }

    @Test
    void getAllUsers_thenInvoked_thenReturnedListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        List<UserDto> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }

    @Test
    void updateUser_WhenInvokedWithNewName_thenUpdateUserName() {
        userDto.setName("UpdName");
        userDto.setEmail("Updemail@email.com");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        UserDto savedUser = userService.updateUser(userDto.getId(), userDto);

        assertEquals(1, savedUser.getId());
        assertEquals("UpdName", savedUser.getName());
        assertEquals("Updemail@email.com", savedUser.getEmail());
    }

    @Test
    void updateUser_WhenInvokedWithNoChanges_thenDoNotUpdateUserName() {
        userDto.setName(null);
        userDto.setEmail(null);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        UserDto savedUser = userService.updateUser(userDto.getId(), userDto);

        assertEquals(1, savedUser.getId());
    }

    @Test
    void deleteUserById_whenInvokedWithCorrectUserId_thenDoesNotThrowAnyException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> userService.deleteUserById(anyLong()));
    }

    @Test
    void checkUserExistence_whenInvokedWithCorrectUserId_thenDoesNotThrowAnyException() {
        when(userRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> userService.checkUserExistence(anyLong()));
    }

    @Test
    void checkUserExistence_whenInvokedWithWrongUserId_thenThrowNoSuchElementException() {
        when(userRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> userService.checkUserExistence(anyLong()));
    }

    @Test
    void findUserById_whenInvokedWithCorrectUserId_thenReturnedUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User user = userService.findUserById(anyLong());

        assertEquals(1, user.getId());
        assertEquals("name", user.getName());
        assertEquals("email@email.com", user.getEmail());
    }

    @Test
    void findUserById_whenInvokedWithWrongUserId_thenThrowNoSuchElementException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.getUserById(anyLong()));
    }
}