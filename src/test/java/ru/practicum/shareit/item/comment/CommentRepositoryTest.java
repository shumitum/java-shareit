package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager tem;

    private Comment comment;
    private User user;
    private Item item;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();
        item = Item.builder()
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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findByItemId() {
        tem.persist(user);
        tem.persist(item);
        tem.persist(comment);

        List<Comment> comments = commentRepository.findByItemId(item.getId());

        assertEquals(1, comments.size());
        assertEquals(comment.getId(), comments.get(0).getId());
    }

    @Test
    void findByItemOwnerId() {
        Comment newComment = Comment.builder()
                .text("text1")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();
        tem.persist(user);
        tem.persist(item);
        tem.persist(comment);
        tem.persist(newComment);

        List<Comment> comments = commentRepository.findByItemOwnerId(user.getId()).stream()
                .sorted(Comparator.comparingLong(Comment::getId))
                .collect(Collectors.toList());

        assertEquals(2, comments.size());
        assertEquals(1L, comments.get(0).getId());
        assertEquals(2L, comments.get(1).getId());
    }
}