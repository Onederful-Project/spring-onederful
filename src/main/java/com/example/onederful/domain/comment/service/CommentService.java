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
    private final JwtUtil jwtUtil;

    public CreateCommentResponseDataDto save(HttpServletRequest httpServletRequest, String contents) {

        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(httpServletRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Comment comment = new Comment(contents, user);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDataDto(savedComment.getId(), user.getName(), savedComment.getContents());

    }

    @Transactional
    public UpdateCommentResponseDataDto updateComment(Long commentId, String contents, HttpServletRequest httpServletRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 토큰에서 Id 가져오기
        Long userId = jwtUtil.extractId(httpServletRequest);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if(comment.getIsDeleted()==true){
            throw new IllegalArgumentException("삭제된 댓글입니다.");
        }

        comment.update(contents);
        return new UpdateCommentResponseDataDto(comment.getId(), user.getName(), comment.getContents(),comment.getCreatedAt(), comment.getUpdatedAt());
    }

    public List<CommentResponseDataDto> findAllCommentByTaskId(Long taskId){
        List<Comment> commentListById = commentRepository.findAllByTaskIdOrderByCreatedAtDesc(taskId);

        return commentListById.stream()
                .filter(comment -> !comment.getIsDeleted())
                .map(CommentResponseDataDto::from)
                .collect(Collectors.toList());
    }


    public List<CommentResponseDataDto> findCommentByContents(String contents){

        List<Comment> commentListByContents = commentRepository.findByContentsLike("%"+contents+"%");

        return commentListByContents.stream()
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
