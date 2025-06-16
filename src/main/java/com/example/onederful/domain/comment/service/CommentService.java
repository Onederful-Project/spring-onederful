package com.example.onederful.domain.comment.service;

import com.example.onederful.domain.comment.dto.CreateCommentResponseDataDto;
import com.example.onederful.domain.comment.dto.ResponseDto;
import com.example.onederful.domain.comment.dto.UpdateCommentResponseDataDto;
import com.example.onederful.domain.comment.entity.Comment;
import com.example.onederful.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CreateCommentResponseDataDto save(String username, String contents) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Comment comment = new Comment(contents, user);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDataDto(savedComment.getId(), "홍길동", savedComment.getContents());

    }

    @Transactional
    public UpdateCommentResponseDataDto updateComment(Long commentId, String contents, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() ->new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        comment.update(contents);
        return new UpdateCommentResponseDataDto(comment.getId(), user.getName(), comment.getContents(),comment.getCreatedAt(), comment.getUpdatedAt());
    }
}
