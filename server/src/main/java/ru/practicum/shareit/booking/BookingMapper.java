package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class})
public interface BookingMapper {
    BookingDto toBookingDto(Booking booking);

    Booking toBooking(BookingDto bookingDto);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingInfoDto toBookingInfoDto(Booking booking);
}