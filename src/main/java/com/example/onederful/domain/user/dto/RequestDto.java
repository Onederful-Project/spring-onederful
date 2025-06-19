package com.example.onederful.domain.user.dto;

import com.example.onederful.domain.user.common.LoginGroup;
import com.example.onederful.domain.user.common.PasswordGroup;
import com.example.onederful.domain.user.common.SignupGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RequestDto {
    @NotBlank(groups = {SignupGroup.class, LoginGroup.class}, message = "사용자명은 필수입니다.")
    @Pattern(groups = {SignupGroup.class, LoginGroup.class},
            regexp = "^[a-zA-Z0-9]{4,20}$",
            message = "사용자명은 4-20자의 영문/숫자만 허용됩니다")
    private String username;

    @NotBlank (groups = {SignupGroup.class} , message = "이메일은 필수입니다.")
    @Email (groups = {SignupGroup.class}, message = "유효한 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank (groups = {SignupGroup.class,LoginGroup.class, PasswordGroup.class} , message = "비밀번호는 필수입니다.")
    @Pattern (groups = {SignupGroup.class,LoginGroup.class, PasswordGroup.class},
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^\\w\\s]).+$",
            message = "비밀번호는 대문자·소문자·숫자·특수문자를 각각 1자 이상 포함해야 합니다."
    )
    private String password;

    @NotBlank (groups = {SignupGroup.class}, message = "이름은 필수입니다.")
    @Size(groups = {SignupGroup.class}, min = 2, max = 50, message = "이름은 2~50자 사이어야 합니다.")
    private String name;
}
