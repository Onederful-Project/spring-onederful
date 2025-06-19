package com.example.onederful.common;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ListResponse<T> {

    private List<T> content;
    private Long totalElements;
    private int totalPages;
    private int size;
    private int number;
}
