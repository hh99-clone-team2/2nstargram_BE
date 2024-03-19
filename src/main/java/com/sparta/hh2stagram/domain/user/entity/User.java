package com.sparta.hh2stagram.domain.user.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Tag(name = "User Table", description = "유저 테이블")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "유저 생성 ID")
    private Long id;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email
    @Schema(description = "사용자 이메일(아이디)", nullable = false, example = "sparta@gmail.com")
    @Column
    private String email;

    @NotNull(message = "비밀번호는 필수 입력 값입니다.")
    @Schema(description = "비밀번호, 8~15자 영어 대소문자, 숫자 특수문자 !@#$%^&*()_만 사용할 수 있습니다.", nullable = false, example = "abcd1234!")
    @Column
    private String password;

    @NotNull(message = "유저이름은 필수 입력 값입니다.")
    @Schema(description = "사용자 이름 입니다.", nullable = false, example = "르탄이")
    @Column
    private String username;

    @Schema(description = "유저 권한", example = "USER")
    @Enumerated(value = EnumType.STRING)
    @Column
    private UserRoleEnum role;

    @Builder
    public User(String email, String password, String username, UserRoleEnum role) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
    }
}
