package com.example.onederful.domain.user.dto;

import com.example.onederful.domain.user.common.LoginGroup;
import com.example.onederful.domain.user.common.PasswordGroup;
import com.example.onederful.domain.user.common.SignupGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestDto {

    @NotBlank(groups = {SignupGroup.class, LoginGroup.class})
    private String username;

    @NotBlank (groups = {SignupGroup.class})
    @Email (groups = {SignupGroup.class}, message = "올바른 형식의 이메일 주소를 입력해주세요")
    private String email;

    @NotBlank (groups = {SignupGroup.class,LoginGroup.class, PasswordGroup.class})
    @Pattern (groups = {SignupGroup.class,LoginGroup.class, PasswordGroup.class},
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).+$",
            message = "비밀번호는 대문자·소문자·숫자·특수문자를 각각 1자 이상 포함해야 합니다."
    )
    private String password;

    @NotBlank (groups = {SignupGroup.class})
    private String name;
}
