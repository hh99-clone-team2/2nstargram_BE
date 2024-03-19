package com.sparta.hh2stagram.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;


@Getter
public class SignupRequestDto {

    @Email
    @NotBlank(message = "이메일")
    private String email;

    @NotNull(message = "비밀번호")
    private String password;

    @NotNull(message = "사용자 이름")
    private String username;

    @Builder
    public SignupRequestDto(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
