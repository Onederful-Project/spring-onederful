package com.example.onederful.domain.comment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @Size(min=1, max = 500, message = "댓글 내용은 1~500자로 입력해주세요.")
    @NotNull(message = "댓글 내용은 필수 항목입니다.")
    private final String content;

    public CommentRequestDto(String content){
        this.content = content;
    }
}
