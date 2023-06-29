package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TestEntityManager tem;

    private Item item;
    private User user;
    private ItemRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        request = ItemRequest.builder()
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        item = Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .owner(user)
                .request(request)
                .build();
        tem.persist(user);
        tem.persist(request);
        tem.persist(item);
    }

    @Test
    void findItem_whenInvokedWithExistItemName_thenReturnedRightItem() {
        List<Item> items = itemRepository.findItem("name", Pageable.unpaged());

        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
        assertEquals("name", items.get(0).getName());
    }

    @Test
    void findAllByOwnerIdOrderByIdAsc_whenInvokedWithExistOwnerId_thenReturnedAllUsersItems() {
        Item newItem = Item.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .owner(user)
                .request(request)
                .build();
        tem.persist(newItem);

        List<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(user.getId(), Pageable.unpaged());

        assertEquals(2, items.size());
        assertEquals(item.getId(), items.get(0).getId());
        assertEquals(newItem.getId(), items.get(1).getId());
    }

    @Test
    void findAllByOwnerIdOrderByIdAsc_whenInvokedWithExistOwnerId_thenReturnedPageWithOneItem() {
        Item newItem = Item.builder()
                .name("name1")
                .description("description1")
                .available(true)
                .owner(user)
                .request(request)
                .build();
        tem.persist(newItem);

        List<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(user.getId(), PageRequest.of(0, 1));

        assertEquals(1, items.size());
    }

    @Test
    void findByRequestId_thenInvokedWithCorrectRequestId_thenReturnedCorrectItemList() {
        List<Item> items = itemRepository.findByRequestId(request.getId());

        assertEquals(1, items.size());
        assertEquals(item.getId(), items.get(0).getId());
    }
}