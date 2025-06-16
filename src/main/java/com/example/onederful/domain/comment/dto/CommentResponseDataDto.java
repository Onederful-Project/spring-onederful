package com.example.onederful.domain.comment.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDataDto {
    private final Long taskId;
    private final String writer;
    private final String contents;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public CommentResponseDataDto(Long taskId, String writer, String contents, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.taskId = taskId;
        this.writer = writer;
        this.contents = contents;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
