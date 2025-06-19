package com.example.onederful.domain.comment.dto;

import com.example.onederful.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CommentResponseDataDto {
    private final Long id;
    private final String content;
    private final Long taskId;
    private final Long userId;
    private final UserData userData;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static CommentResponseDataDto of(Comment comment){
        return CommentResponseDataDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .userData(UserData.of(comment.getUser()))
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }


    public static CommentResponseDataDto from(Comment comment){
        return new CommentResponseDataDto(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getUser().getId(),
                UserData.of(comment.getUser()),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
