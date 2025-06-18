package com.example.onederful.domain.comment.service;

import com.example.onederful.domain.comment.dto.CreateCommentResponseDataDto;
import com.example.onederful.domain.comment.entity.Comment;
import com.example.onederful.domain.comment.repository.CommentRepository;
import com.example.onederful.domain.task.entity.Task;
import com.example.onederful.domain.task.repository.TaskRepository;
import com.example.onederful.domain.user.entity.User;
import com.example.onederful.domain.user.repository.UserRepository;
import com.example.onederful.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("지정된 테스크에 HttpServletRequest에서 사용자 정보를 가져와서 댓글을 생성 할 수 있는지")
    void save() {

        // given
        Long task_id = 5L;
        Long user_id = 2L;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        String content = "댓글 생성 테스트";

        User user = mock(User.class);
        Task task = mock(Task.class);
        Comment comment = Comment.builder()
                .id(1L)
                .content(content)
                .user(user)
                .task(task)
                .build();


        given(jwtUtil.extractId(httpServletRequest)).willReturn(user_id);

        given(userRepository.findById(user_id)).willReturn(Optional.of(user));
        given(taskRepository.findById(task_id)).willReturn(Optional.of(task));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);
        // when
        CreateCommentResponseDataDto result = commentService.save(task_id, httpServletRequest, content);

        // then
        assertThat(result.getContent()).isEqualTo(content);
        assertThat(result.getId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("새로운 댓글내용 입력했을때 그 값으로 변하는지 안하는지")
    void updateComment() {
        // given



        // when


        // then




    }

    @Test
    void findAllCommentByTaskId() {
    }

    @Test
    void findAllCommentByTaskIdInPage() {
    }

    @Test
    void findCommentByContent() {
    }

    @Test
    void deleteComment() {
    }
}