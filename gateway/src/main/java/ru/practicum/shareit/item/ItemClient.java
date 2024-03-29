package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> createItem(ItemDto itemDto, long userId) {
        return post("", userId, itemDto);
    }

    public ResponseEntity<Object> updateItem(long itemId, ItemDto itemDto, long ownerId) {
        return patch(String.format("/%d", itemId), ownerId, itemDto);
    }

    public ResponseEntity<Object> getItemById(long itemId, long userId) {
        return get(String.format("/%d", itemId), userId);
    }

    public ResponseEntity<Object> getUserItems(long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> searchItem(String searchRequest, long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "searchRequest", searchRequest,
                "from", from,
                "size", size
        );
        return get("/search?text={searchRequest}&from={from}&size={size}", userId, parameters);
    }

    public void deleteItemById(long itemId, long userId) {
        delete(String.format("/%d", itemId), userId);
    }
}