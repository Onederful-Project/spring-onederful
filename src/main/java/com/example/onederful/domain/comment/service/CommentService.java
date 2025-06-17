package com.example.onederful.domain.comment.service;

import com.example.onederful.domain.comment.dto.CommentResponseDataDto;
import com.example.onederful.domain.comment.dto.CreateCommentResponseDataDto;
import com.example.onederful.domain.comment.dto.ResponseDto;
import com.example.onederful.domain.comment.dto.UpdateCommentResponseDataDto;
import com.example.onederful.domain.comment.entity.Comment;
import com.example.onederful.domain.comment.repository.CommentRepository;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final JwtUtil jwtUtil;

    public CreateCommentResponseDataDto save(Long task_id, HttpServletRequest httpServletRequest, String content) {

        // 토큰에서 Id 가져오기
        Long user_id = jwtUtil.extractId(httpServletRequest);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Task task = taskRepository.findById(task_id)
                .orElseThrow(() -> new IllegalArgumentException("테스크를 찾을 수 없습니다."));

        Comment comment = new Comment(content, user, task);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDataDto(savedComment.getId(), user.getName(), savedComment.getContent());

    }

    @Transactional
    public UpdateCommentResponseDataDto updateComment(Long comment_id, String content, HttpServletRequest httpServletRequest) {
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() ->new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(httpServletRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(comment.getIsDeleted()==true){
            throw new IllegalArgumentException("삭제된 댓글입니다.");
        }

        comment.update(content);
        return new UpdateCommentResponseDataDto(comment.getId(), user.getName(), comment.getContent(),comment.getCreatedAt(), comment.getUpdatedAt());
    }

    public List<CommentResponseDataDto> findAllCommentByTaskId(Long taskId){
        List<Comment> commentListById = commentRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId);

        return commentListById.stream()
                .filter(comment -> !comment.getIsDeleted())
                .map(CommentResponseDataDto::from)
                .collect(Collectors.toList());
    }


    public List<CommentResponseDataDto> findCommentByContent(String content){

        List<Comment> commentListByContent = commentRepository.findBycontentLike("%"+content+"%");

        return commentListByContent.stream()
                .filter(comment -> !comment.getIsDeleted())
                .map(CommentResponseDataDto::from)
                .collect(Collectors.toList());
    }


    @Transactional
    public void deleteComment(Long commentId){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        comment.delete();
    }
}
