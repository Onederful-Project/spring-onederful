package com.example.onederful.domain.comment.repository;

import com.example.onederful.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTaskIdOrderByCreatedAtDesc(Long taskId);

    List<Comment> findByContentsLike(String contents);
}
