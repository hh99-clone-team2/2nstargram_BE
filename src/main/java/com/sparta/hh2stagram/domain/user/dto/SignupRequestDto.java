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
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_~]).{8,15}$", message = "비밀번호는 영어 대소문자, 숫자, 특수문자 !@#$%^&*()_~로 이루어진 8~15자리 입니다.")
    private String password;

    @NotNull(message = "성명")
    private String name;

    @NotNull(message = "사용자 이름")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[._-])", message = "사용자 이름은 영어 대소문자, 특수문자 ._-로 이루어져야 합니다.")
    private String username;



    @Builder
    public SignupRequestDto(String loginId, String password, String name, String username) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.username=username;
    }
}
