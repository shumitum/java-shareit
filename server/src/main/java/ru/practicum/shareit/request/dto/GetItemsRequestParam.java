package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetItemsRequestParam {
    private long userId;
    private Integer from;
    private Integer size;
    private PageRequest page;

    public static GetItemsRequestParam of(long userId, Integer from, Integer size) {
        return GetItemsRequestParam.builder()
                .userId(userId)
                .from(from)
                .size(size)
                .build();
    }

    public PageRequest getPage() {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}