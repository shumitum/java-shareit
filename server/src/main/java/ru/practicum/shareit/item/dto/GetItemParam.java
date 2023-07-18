package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetItemParam {
    private long userId;
    private Integer from;
    private Integer size;

    public static GetItemParam of(long userId, Integer from, Integer size) {
        return GetItemParam.builder()
                .userId(userId)
                .from(from)
                .size(size)
                .build();
    }

    public PageRequest getPage() {
        return PageRequest.of(from > 0 ? from / size : 0, size);
    }
}