package com.example.onederful.domain.comment.controller;

import com.example.onederful.common.ApiResponseDto;
import com.example.onederful.domain.comment.dto.*;
import com.example.onederful.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<ApiResponseDto> save(@PathVariable Long task_id, HttpServletRequest httpServletRequest, @RequestBody CommentRequestDto requestDto){

        CreateCommentResponseDataDto createCommentResponseDataDto =
                commentService.save(task_id, httpServletRequest, requestDto.getContent());

        ApiResponseDto success = ApiResponseDto.success("댓글이 생성되었습니다,", createCommentResponseDataDto);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 댓글 수정
    @PatchMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ApiResponseDto> updateComment(
            @PathVariable Long task_id, @PathVariable Long comment_id, @RequestBody CommentRequestDto requestDto, HttpServletRequest httpServletRequest
    ) {

        UpdateCommentResponseDataDto updateCommentResponseDataDto =
                commentService.updateComment(task_id, comment_id, requestDto.getContent(), httpServletRequest);

        ApiResponseDto success = ApiResponseDto.success("댓글이 수정되었습니다.", updateCommentResponseDataDto);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 테스크별 댓글 조회
    @GetMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ApiResponseDto> findAllCommentByTaskId(
            @PathVariable Long task_id) {

        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findAllCommentByTaskId(task_id);

        ApiResponseDto success = ApiResponseDto.success("task " + task_id + "에 달린 댓글 목록", commentResponseDataDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 테스크별 댓글 조회 페이지화
    @GetMapping("/tasks/{task_id}/comments/pages")
    public ResponseEntity<ApiResponseDto> findAllCommentByTaskIdInPage(
            @PathVariable Long task_id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<CommentResponseDataDto> commentResponseDtoInPage = commentService.findAllCommentByTaskIdInPage(task_id,pageable);

        ApiResponseDto success = ApiResponseDto.success("task " + task_id + "에 달린 댓글 목록", commentResponseDtoInPage);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 내용으로 댓글 조회
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto> findCommentByContent(@RequestBody CommentRequestDto requestDto) {

        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findCommentByContent(requestDto.getContent());

        ApiResponseDto success = ApiResponseDto.success(requestDto.getContent() + "가 포함된 댓글 목록 ", commentResponseDataDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }


    // 댓글 삭제
    @DeleteMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long task_id, @PathVariable Long comment_id) {

        commentService.deleteComment(comment_id);

        ApiResponseDto success = ApiResponseDto.success("댓글이 삭제되었습니다.", null);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

}
