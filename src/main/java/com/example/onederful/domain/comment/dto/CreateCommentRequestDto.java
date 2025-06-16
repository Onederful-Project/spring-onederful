package com.example.onederful.domain.comment.dto;

import lombok.Getter;

@Getter
public class CreateCommentRequestDto {

    private final String contents;

    public CreateCommentRequestDto(String writer, String contents) {
        this.contents = contents;
    }

}
