package com.sparta.hh2stagram.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;


@Getter
public class SignupRequestDto {

    @NotBlank(message = "이메일 또는 전화번호")
    private String loginId;

    @NotNull(message = "비밀번호")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_~]).{8,15}$")
    private String password;

    @NotNull(message = "사용자 이름")
    private String name;

    @NotNull(message = "닉네임")
    private String nickname;



    @Builder
    public SignupRequestDto(String loginId, String password, String name, String nickname) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.nickname=nickname;
    }
}
