package com.example.onederful.domain.comment.service;

import com.example.onederful.common.ListResponse;
import com.example.onederful.domain.comment.dto.CommentResponseDataDto;
import com.example.onederful.domain.comment.entity.Comment;
import com.example.onederful.domain.comment.repository.CommentRepository;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.exception.CustomException;
import com.example.onederful.exception.ErrorCode;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;

    // 댓글 생성
    public CommentResponseDataDto save(Long task_id, HttpServletRequest httpServletRequest,
        String content) {

        // 토큰에서 Id 가져오기
        Long user_id = jwtUtil.extractId(httpServletRequest);

        User user = userRepository.findById(user_id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));

        Task task = taskRepository.findById(task_id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));

        Comment comment = new Comment(content, user, task);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponseDataDto.of(savedComment);

    }

    // 댓글 수정
    @Transactional
    public CommentResponseDataDto updateComment(Long task_id, Long comment_id, String content,
        HttpServletRequest httpServletRequest) {
        Task task = taskRepository.findById(task_id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_TASK));

        Comment comment = commentRepository.findById(comment_id)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_COMMENT));

        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(httpServletRequest);

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_USER));

        if (comment.getIsDeleted()) {
            throw new CustomException(ErrorCode.INVALID_COMMENT);
        }

        comment.update(content);
        return CommentResponseDataDto.of(comment);
    }

    // 댓글 조회 (테스크별)
    public ListResponse<CommentResponseDataDto> findAllCommentByTaskIdInPage(Long task_id,
        Pageable pageable) {
        // 페이징 대상 조회
        final Page<Comment> commentListByIdInPage = commentRepository.findByTaskIdAndIsDeletedFalse(
            task_id, pageable);

        return ListResponse.<CommentResponseDataDto>builder()
            .content(commentListByIdInPage.getContent().stream().map(CommentResponseDataDto::of)
                .collect(Collectors.toList()))
            .size(commentListByIdInPage.getSize())
            .number(commentListByIdInPage.getNumber())
            .totalElements(commentListByIdInPage.getTotalElements())
            .totalPages(commentListByIdInPage.getTotalPages())
            .build();
    }

    // 댓글 조회 (내용 검색)
    public List<CommentResponseDataDto> findCommentByContent(String content) {

        // 찾는 내용을 댓글을 적은 사람과 댓글 내용에서 검색
        List<Comment> commentListByContent = commentRepository.findByContentOrUsername(
            "%" + content + "%");

        return commentListByContent.stream()
            .filter(comment -> !comment.getIsDeleted())
            .map(CommentResponseDataDto::from)
            .collect(Collectors.toList());
    }


    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new CustomException(ErrorCode.NONEXISTENT_COMMENT));

        comment.delete();
    }
}
