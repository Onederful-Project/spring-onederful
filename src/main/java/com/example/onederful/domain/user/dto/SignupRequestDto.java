package com.example.onederful.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class SignupRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    @Email(message = "올바른 형식의 이메일 주소를 입력해주세요")
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).+$",
            message = "비밀번호는 대문자·소문자·숫자·특수문자를 각각 1자 이상 포함해야 합니다."
    )
    private String password;

    @NotBlank
    private String name;
}
