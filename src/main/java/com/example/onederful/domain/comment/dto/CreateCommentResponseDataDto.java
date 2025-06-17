package com.example.onederful.domain.comment.dto;

import lombok.Getter;

@Getter
public class CreateCommentResponseDataDto {

    private final Long id;
    private final String writer;
    private final String content;

    public CreateCommentResponseDataDto(Long id, String writer, String content){
        this.id = id;
        this.writer = writer;
        this.content = content;
    }

}
