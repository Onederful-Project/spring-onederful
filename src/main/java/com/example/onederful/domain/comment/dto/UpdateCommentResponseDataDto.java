package com.example.onederful.domain.comment.dto;

import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponseDataDto {

    private final Long id;
    private final String writer;
    private final String contents;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;


    public UpdateCommentResponseDataDto(Long id, String writer, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.writer = writer;
        this.contents = contents;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
    }
}
