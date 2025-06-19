package com.example.onederful.domain.comment.controller;

import com.example.onederful.common.ApiResponseDto;
import com.example.onederful.common.ListResponse;
import com.example.onederful.domain.comment.dto.CommentRequestDto;
import com.example.onederful.domain.comment.dto.CommentResponseDataDto;
import com.example.onederful.domain.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ApiResponseDto> save(@PathVariable Long task_id,
        HttpServletRequest httpServletRequest, @RequestBody CommentRequestDto requestDto) {

        CommentResponseDataDto CommentResponseDataDto =
            commentService.save(task_id, httpServletRequest, requestDto.getContent());

        ApiResponseDto success = ApiResponseDto.success("댓글이 생성되었습니다,", CommentResponseDataDto);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 댓글 수정
    @PutMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ApiResponseDto> updateComment(
        @PathVariable Long task_id, @PathVariable Long comment_id,
        @RequestBody CommentRequestDto requestDto, HttpServletRequest httpServletRequest
    ) {

        CommentResponseDataDto CommentResponseDataDto =
            commentService.updateComment(task_id, comment_id, requestDto.getContent(),
                httpServletRequest);

        ApiResponseDto success = ApiResponseDto.success("댓글이 수정되었습니다.", CommentResponseDataDto);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 댓글 조회 (테스크별)
    @GetMapping("/tasks/{task_id}/comments")
    public ResponseEntity<ApiResponseDto> findAllCommentByTaskIdInPage(
        @PathVariable Long task_id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        ListResponse<CommentResponseDataDto> commentResponseDtoInPage = commentService.findAllCommentByTaskIdInPage(
            task_id, pageable);

        ApiResponseDto success = ApiResponseDto.success("댓글 목록을 조회했습니다.", commentResponseDtoInPage);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

    // 댓글 조회 (내용 검색)
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDto> findCommentByContent(
        @RequestBody CommentRequestDto requestDto) {

        List<CommentResponseDataDto> commentResponseDataDtoList = commentService.findCommentByContent(
            requestDto.getContent());

        ApiResponseDto success = ApiResponseDto.success(requestDto.getContent() + "가 포함된 댓글 목록 ",
            commentResponseDataDtoList);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }


    // 댓글 삭제
    @DeleteMapping("/tasks/{task_id}/comments/{comment_id}")
    public ResponseEntity<ApiResponseDto> deleteComment(@PathVariable Long task_id,
        @PathVariable Long comment_id) {

        commentService.deleteComment(comment_id);

        ApiResponseDto success = ApiResponseDto.success("댓글이 삭제되었습니다.", null);

        return ResponseEntity.status(HttpStatus.OK).body(success);
    }

}
