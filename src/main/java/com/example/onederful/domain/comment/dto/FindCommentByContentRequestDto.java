package com.example.onederful.domain.comment.dto;

import lombok.Getter;

@Getter
public class FindCommentByContentRequestDto {

    private final String content;


    public FindCommentByContentRequestDto(String content) {
        this.content = content;
    }
}
