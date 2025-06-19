package com.example.onederful.domain.comment.dto;


import com.example.onederful.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserData {

    private Long id;
    private String username;
    private String name;
    private String email;

    public static UserData of(User user) {
        return UserData.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
