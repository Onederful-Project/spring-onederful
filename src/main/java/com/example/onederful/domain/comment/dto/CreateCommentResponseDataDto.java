package com.example.onederful.domain.comment.dto;

import lombok.Getter;

@Getter
public class CreateCommentResponseDataDto {

    private final Long id;
    private final String writer;
    private final String contents;

    public CreateCommentResponseDataDto(Long id, String writer, String contents){
        this.id = id;
        this.writer = writer;
        this.contents = contents;
    }

}
