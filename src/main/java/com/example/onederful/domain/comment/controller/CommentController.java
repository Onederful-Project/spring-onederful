package com.example.onederful.domain.comment.controller;

import com.example.onederful.domain.comment.dto.*;
import com.example.onederful.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ResponseDto<CreateCommentResponseDataDto>> save (@PathVariable Long task_id, HttpServletRequest httpServletRequest, @RequestBody CreateCommentRequestDto requestDto){

        CreateCommentResponseDataDto createCommentResponseDataDto =
                commentService.save(task_id,httpServletRequest, requestDto.getContent());
        ResponseDto<CreateCommentResponseDataDto> responseDto = ResponseDto.success("댓글이 생성되었습니다.", createCommentResponseDataDto);

        return ResponseEntity.ok(responseDto);
    }

    // 댓글 수정
    @PatchMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ResponseDto<UpdateCommentResponseDataDto>> updateComment(
            @PathVariable Long task_id, @PathVariable Long comment_id, @RequestBody UpdateCommentRequestDto requestDto, HttpServletRequest httpServletRequest
    ) {
        UpdateCommentResponseDataDto updateCommentResponseDataDto =
                commentService.updateComment(comment_id, requestDto.getContent(), httpServletRequest);
        return ResponseEntity.ok(ResponseDto.success("댓글이 수정되었습니다.", updateCommentResponseDataDto));
    }

    // 테스크별 댓글 조회
    @GetMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findAllCommentByTaskId(
            @PathVariable Long task_id) {
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findAllCommentByTaskId(task_id);
        return ResponseEntity.ok(ResponseDto.success("task " + task_id + "에 달린 댓글 목록", commentResponseDataDtoList));
    }

    // 내용으로 댓글 조회
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findCommentByContent(@RequestBody FindCommentByContentRequestDto requestDto){
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findCommentByContent(requestDto.getContent());
        return ResponseEntity.ok(ResponseDto.success( requestDto.getContent() + "가 포함된 댓글 목록 ", commentResponseDataDtoList));
    }


    // 댓글 삭제
    @DeleteMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(@PathVariable Long task_id, @PathVariable Long comment_id){
        commentService.deleteComment(comment_id);
        return ResponseEntity.ok(ResponseDto.success("댓글이 삭제되었습니다.", null));
    }

}
