package com.example.onederful.domain.task.repository;

import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:keyword% OR t.description LIKE %:keyword%) AND t.status = :status")
    Page<Task> findTasks(@Param("keyword") String keyword, @Param("status") ProcessStatus status,
        Pageable pageable);
}
