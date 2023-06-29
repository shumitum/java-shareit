package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRequestRepositoryTest {

    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    TestEntityManager tem;

    private ItemRequest request;

    private User requestor;

    @BeforeEach
    void setUp() {
        requestor = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        request = ItemRequest.builder()
                .description("description")
                .requestor(requestor)
                .created(LocalDateTime.now())
                .build();
        tem.persist(requestor);
        tem.persist(request);
    }

    @Test
    void findByRequestorIdOrderByCreatedDesc_whenInvoked_thenReturnListOfOneItemRequest() {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(requestor.getId());

        assertEquals(1, itemRequests.size());
        assertEquals(request.getId(), itemRequests.get(0).getId());
        assertEquals("description", itemRequests.get(0).getDescription());
    }

    @Test
    void findByRequestorIdOrderByCreatedDesc_whenInvoked_thenReturnEmptyList() {
        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(999L);

        assertEquals(0, itemRequests.size());
    }

    @Test
    void findItemRequestsByRequestorIdNotOrderByCreatedDesc_whenInvoked_thenReturnListOfOneItemRequest() {
        List<ItemRequest> itemRequests = itemRequestRepository.findItemRequestsByRequestorIdNotOrderByCreatedDesc(999L, Pageable.unpaged());

        assertEquals(1, itemRequests.size());
        assertEquals(request.getId(), itemRequests.get(0).getId());
    }

    @Test
    void findItemRequestsByRequestorIdNotOrderByCreatedDesc_whenInvoked_thenReturnEmptyList() {
        List<ItemRequest> itemRequests = itemRequestRepository
                .findItemRequestsByRequestorIdNotOrderByCreatedDesc(requestor.getId(), Pageable.unpaged());

        assertEquals(0, itemRequests.size());
    }
}