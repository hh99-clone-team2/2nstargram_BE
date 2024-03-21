package com.sparta.hh2stagram.domain.user.dto;

import com.sparta.hh2stagram.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String name;

    @Builder
    public UserResponseDto(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public UserResponseDto(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
    }
}
