package com.example.onederful.domain.comment.dto;

import lombok.Getter;

@Getter
public class UpdateCommentRequestDto {

    private final String content;

    public UpdateCommentRequestDto(String content){
        this.content = content;
    }
}
