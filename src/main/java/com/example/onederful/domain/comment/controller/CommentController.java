package com.example.onederful.domain.comment.controller;

import com.example.onederful.domain.comment.dto.*;
import com.example.onederful.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/comments")
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

    // 댓글 수정
    @PatchMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseDto<UpdateCommentResponseDataDto>> updateComment(
            @PathVariable Long id, @RequestBody UpdateCommentRequestDto requestDto, @AuthenticationPrincipal AuthUser authUser
    ) {
        Long userId = authUser.getUserId();
        UpdateCommentResponseDataDto updateCommentResponseDataDto =
                commentService.updateComment(id, requestDto.getContents(), userId);
        return ResponseEntity.ok(ResponseDto.success("댓글이 수정되었습니다.", updateCommentResponseDataDto));
    }

    // 테스크별 댓글 조회
    @GetMapping("/comments/task/{task_id}")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findAllCommentByTaskId(
            @PathVariable Long taskId) {
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findAllCommentByTaskId(taskId);
        return ResponseEntity.ok(ResponseDto.success("데스크 " + taskId + "에 달린 댓글 목록", commentResponseDataDtoList));
    }

    // 내용으로 댓글 조회
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findCommentByContents(@RequestBody String contents){
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findCommentByContents(contents);
        return ResponseEntity.ok(ResponseDto.success( contents + "가 포함된 댓글 목록 ", commentResponseDataDtoList));
    }




    // 댓글 삭제
    @DeleteMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ResponseDto.success("댓글이 삭제되었습니다.", null));
    }

}
