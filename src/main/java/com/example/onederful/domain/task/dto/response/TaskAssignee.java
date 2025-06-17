package com.example.onederful.domain.task.dto.response;

import com.example.onederful.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskAssignee {

    private Long id;
    private String username;
    private String name;
    private String email;

    public static TaskAssignee of(User user) {
        return TaskAssignee.builder()
            .id(user.getId())
            .username(user.getUsername())
            .name(user.getName())
            .email(user.getEmail())
            .build();
    }
}
