package com.example.onederful.domain.comment.controller;

import com.example.onederful.domain.comment.dto.CreateCommentRequestDto;
import com.example.onederful.domain.comment.dto.CreateCommentResponseDataDto;
import com.example.onederful.domain.comment.dto.ResponseDto;
import com.example.onederful.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    //public ResponseEntity<CreateCommentResponseDto> save (@AuthenticationPrincipal AuthUser authUser, @RequestBody CreateCommentRequestDto requestDto){
    public ResponseEntity<ResponseDto<CreateCommentResponseDataDto>> save(
            String username, @RequestBody CreateCommentRequestDto requestDto) {
//        Long userId = authUser.getUserId();
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        username = "홍길동";

        CreateCommentResponseDataDto createCommentResponseDataDto =
                commentService.save(username, requestDto.getContents());
        ResponseDto<CreateCommentResponseDataDto> responseDto = ResponseDto.success("댓글이 생성되었습니다.", createCommentResponseDataDto);

        return ResponseEntity.ok(responseDto);
    }
}
