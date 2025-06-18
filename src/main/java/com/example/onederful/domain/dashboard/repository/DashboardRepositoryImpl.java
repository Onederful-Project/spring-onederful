package com.example.onederful.domain.dashboard.repository;

import com.example.onederful.domain.dashboard.dto.MyTasksTodayResponseDto;
import com.example.onederful.domain.dashboard.dto.StatisticsResponseDto;
import com.example.onederful.domain.task.entity.QTask;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.enums.ProcessStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JPAQueryFactory queryFactory;

    public DashboardRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    QTask task;

    @Override
    public StatisticsResponseDto getStatistics(){

        task = QTask.task;

        Long total = Optional.ofNullable(
                queryFactory
                        .select(task.count())
                        .from(task)
                        .where(task.isDeleted.isFalse())
                        .fetchOne()
        ).orElse(0L);

        Long todo = Optional.ofNullable(
                queryFactory
                        .select(task.count())
                        .from(task)
                        .where(
                                task.status.eq(ProcessStatus.TODO),
                                task.isDeleted.isFalse()
                        )
                        .fetchOne()
        ).orElse(0L);;

        Long inProgress = Optional.ofNullable(
                queryFactory
                        .select(task.count())
                        .from(task)
                        .where(
                                task.status.eq(ProcessStatus.IN_PROGRESS),
                                task.isDeleted.isFalse()
                        )
                        .fetchOne()
        ).orElse(0L);

        Long done = Optional.ofNullable(
                queryFactory
                        .select(task.count())
                        .from(task)
                        .where(
                                task.status.eq(ProcessStatus.DONE),
                                task.isDeleted.isFalse()
                        )
                        .fetchOne()
        ).orElse(0L);

        Long overdue = Optional.ofNullable(
                queryFactory
                        .select(task.count())
                        .from(task)
                        .where(
                                task.isDeleted.isFalse(),
                                task.status.in(ProcessStatus.TODO, ProcessStatus.IN_PROGRESS),
                                task.dueDate.before(LocalDateTime.now())
                        )
                        .fetchOne()
        ).orElse(0L);

        double taskDoneRate = 0.0;
        if(total != 0L){
            taskDoneRate = (double) done / total * 100;
        }

        return StatisticsResponseDto.builder()
                .totalTaskCount(total)
                .todoTaskCount(todo)
                .inProgressTaskCount(inProgress)
                .doneTaskCount(done)
                .taskDoneRate(taskDoneRate)
                .overdueTaskCount(overdue)
                .build();
    }

    @Override
    public List<Task> getMyTasksToday(Long userId){
        task = QTask.task;

        List<Task> taskList = queryFactory
                .select(task)
                .from(task)
                .where(
                        task.assignee.id.eq(userId),
                        task.isDeleted.isFalse(),
                        task.status.in(ProcessStatus.TODO, ProcessStatus.IN_PROGRESS)
                )
                .orderBy(task.priority.desc())
                .fetch();

        return taskList;
    }
}
