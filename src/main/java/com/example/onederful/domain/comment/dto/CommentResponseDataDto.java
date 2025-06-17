package com.example.onederful.domain.comment.dto;

import com.example.onederful.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CommentResponseDataDto {
    private final String writer;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;


    public CommentResponseDataDto(String writer, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.writer = writer;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentResponseDataDto from(Comment comment){
        return new CommentResponseDataDto(
                comment.getUser().getName(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
