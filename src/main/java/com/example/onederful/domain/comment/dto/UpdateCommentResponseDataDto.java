package com.example.onederful.domain.comment.dto;

import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class UpdateCommentResponseDataDto {

    private final Long id;
    private final String writer;
    private final String content;
    private final LocalDateTime created_at;
    private final LocalDateTime updated_at;


    public UpdateCommentResponseDataDto(Long id, String writer, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.writer = writer;
        this.content = content;
        this.created_at = createdAt;
        this.updated_at = updatedAt;
    }
}
