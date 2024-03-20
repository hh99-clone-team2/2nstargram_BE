package com.sparta.hh2stagram.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class LoginRequestDto {

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @Builder
    public LoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
