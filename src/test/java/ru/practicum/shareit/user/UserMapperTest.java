package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toUser_whenInvokedWithNotNullUserDto_thenReturnedUser() {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();

        User user = userMapper.toUser(userDto);

        assertEquals(userDto.getId(), user.getId());
        assertEquals(userDto.getName(), user.getName());
        assertEquals(userDto.getEmail(), user.getEmail());

    }

    @Test
    void toUser_whenInvokedWithNullUserDto_thenNull() {
        UserDto userDto = null;

        User user = userMapper.toUser(userDto);

        assertNull(user);

    }

    @Test
    void toUserDto_whenInvokedWithNotNullUser_thenReturnedUserDto() {
        User user = User.builder()
                .id(1L)
                .name("name")
                .email("email@email.com")
                .build();

        UserDto userDto = userMapper.toUserDto(user);

        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
    }

    @Test
    void toUserDto_whenInvokedWithNullUser_thenNull() {
        User user = null;

        UserDto userDto = userMapper.toUserDto(user);

        assertNull(userDto);
    }
}