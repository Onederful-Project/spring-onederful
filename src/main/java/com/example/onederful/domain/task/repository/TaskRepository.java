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

    @Query("SELECT t FROM Task t WHERE (t.title LIKE %:search% OR t.description LIKE %:search%) AND t.status = :status")
    Page<Task> findTasks(@Param("search") String search, @Param("status") ProcessStatus status,
        Pageable pageable);
}
