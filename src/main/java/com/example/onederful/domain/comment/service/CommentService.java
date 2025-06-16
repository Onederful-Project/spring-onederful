package com.example.onederful.domain.comment.service;

import com.example.onederful.domain.comment.dto.CreateCommentResponseDataDto;
import com.example.onederful.domain.comment.dto.ResponseDto;
import com.example.onederful.domain.comment.entity.Comment;
import com.example.onederful.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CreateCommentResponseDataDto save(String username, String contents){

        User user = userRepository.findById(authUser.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Comment comment = new Comment(contents, user);
        Comment savedComment = commentRepository.save(comment);

        return new CreateCommentResponseDataDto(savedComment.getId(), "홍길동", savedComment.getContents());

    }
}
