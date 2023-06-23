package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByBookerIdOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findByBookerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerIdOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerIdAndEndBeforeOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerIdAndStartAfterOrderByStartDesc() {
    }

    @Test
    void findByItemOwnerIdAndStatusOrderByStartDesc() {
    }

    @Test
    void findByItemIdAndStartBeforeAndStatusOrderByStartDesc() {
    }

    @Test
    void findByItemIdAndStartAfterAndStatusOrderByStartAsc() {
    }

    @Test
    void findByBookerIdAndItemIdAndEndBefore() {
    }
}