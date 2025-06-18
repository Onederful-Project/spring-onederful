package com.example.onederful.domain.comment.repository;

import com.example.onederful.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByTaskIdOrderByCreatedAtDesc(Long taskId);
    Page<Comment> findByTaskIdAndIsDeletedFalse(Long task_id, Pageable pageable);

    @Query("SELECT c from Comment c WHERE(c.content LIKE %:keyword% OR c.user.username LIKE %:keyword%) AND c.isDeleted = false")
    List<Comment> findByContentOrUsername(@Param("keyword") String keyword);
}
