package com.sparta.hh2stagram.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    @NotBlank
    private String username;

    @Builder
    public UserResponseDto(String username) {
        this.username = username;
    }
}
