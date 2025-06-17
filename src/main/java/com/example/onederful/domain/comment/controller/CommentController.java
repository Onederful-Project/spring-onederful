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
    @PostMapping("/comments")
    public ResponseEntity<ResponseDto<CreateCommentResponseDataDto>> save (HttpServletRequest httpServletRequest, @RequestBody CreateCommentRequestDto requestDto){

        CreateCommentResponseDataDto createCommentResponseDataDto =
                commentService.save(httpServletRequest, requestDto.getContents());
        ResponseDto<CreateCommentResponseDataDto> responseDto = ResponseDto.success("댓글이 생성되었습니다.", createCommentResponseDataDto);

        return ResponseEntity.ok(responseDto);
    }

    // 댓글 수정
    @PatchMapping("/comments/{comment_id}")
    public ResponseEntity<ResponseDto<UpdateCommentResponseDataDto>> updateComment(
            @PathVariable Long commentId, @RequestBody UpdateCommentRequestDto requestDto, HttpServletRequest httpServletRequest
    ) {
        UpdateCommentResponseDataDto updateCommentResponseDataDto =
                commentService.updateComment(commentId, requestDto.getContents(), httpServletRequest);
        return ResponseEntity.ok(ResponseDto.success("댓글이 수정되었습니다.", updateCommentResponseDataDto));
    }

    // 테스크별 댓글 조회
    @GetMapping("/comments/task/{task_id}")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findAllCommentByTaskId(
            @PathVariable Long taskId) {
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findAllCommentByTaskId(taskId);
        return ResponseEntity.ok(ResponseDto.success("task " + taskId + "에 달린 댓글 목록", commentResponseDataDtoList));
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
