package com.example.onederful.domain.comment.controller;

import com.example.onederful.domain.comment.dto.*;
import com.example.onederful.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<ResponseDto<CreateCommentResponseDataDto>> save(@PathVariable Long task_id, HttpServletRequest httpServletRequest, @RequestBody CommentRequestDto requestDto) {

        CreateCommentResponseDataDto createCommentResponseDataDto =
                commentService.save(task_id, httpServletRequest, requestDto.getContent());
        return ResponseEntity.ok(ResponseDto.success("댓글이 생성되었습니다.", createCommentResponseDataDto));
    }

    // 댓글 수정
    @PatchMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ResponseDto<UpdateCommentResponseDataDto>> updateComment(
            @PathVariable Long task_id, @PathVariable Long comment_id, @RequestBody CommentRequestDto requestDto, HttpServletRequest httpServletRequest
    ) {
        UpdateCommentResponseDataDto updateCommentResponseDataDto =
                commentService.updateComment(task_id, comment_id, requestDto.getContent(), httpServletRequest);
        return ResponseEntity.ok(ResponseDto.success("댓글이 수정되었습니다.", updateCommentResponseDataDto));
    }

    // 테스크별 댓글 조회
    @GetMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findAllCommentByTaskId(
            @PathVariable Long task_id) {
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findAllCommentByTaskId(task_id);
        return ResponseEntity.ok(ResponseDto.success("task " + task_id + "에 달린 댓글 목록", commentResponseDataDtoList));
    }

    // 테스크별 댓글 조회 페이지화
    @GetMapping("/tasks/{task_id}/comments/pages")
    public ResponseEntity<ResponseDto<Page<CommentResponseDataDto>>> findAllCommentByTaskIdInPage(
            @PathVariable Long task_id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<CommentResponseDataDto> commentResponseDtoInPage = commentService.findAllCommentByTaskIdInPage(task_id,pageable);
        return ResponseEntity.ok(ResponseDto.success("task " + task_id + "에 달린 댓글 목록", commentResponseDtoInPage));
    }

    // 내용으로 댓글 조회
    @GetMapping("/search")
    public ResponseEntity<ResponseDto<List<CommentResponseDataDto>>> findCommentByContent(@RequestBody CommentRequestDto requestDto) {
        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findCommentByContent(requestDto.getContent());
        return ResponseEntity.ok(ResponseDto.success(requestDto.getContent() + "가 포함된 댓글 목록 ", commentResponseDataDtoList));
    }


    // 댓글 삭제
    @DeleteMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(@PathVariable Long task_id, @PathVariable Long comment_id) {
        commentService.deleteComment(comment_id);
        return ResponseEntity.ok(ResponseDto.success("댓글이 삭제되었습니다.", null));
    }

}
