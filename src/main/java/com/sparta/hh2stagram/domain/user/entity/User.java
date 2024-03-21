package com.sparta.hh2stagram.domain.user.entity;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
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

import java.util.List;

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

    @Schema(description = "사용자 이메일", example = "sparta@gmail.com")
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    @Schema(description = "사용자 휴대폰 번호", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "비밀번호, 8~15자 영어 대소문자, 숫자 특수문자 !@#$%^&*()_만 사용할 수 있습니다.", example = "abcd1234!")
    @Column
    private String password;

    @Schema(description = "성명 입니다.", example = "홍길동")
    @Column
    private String name;

    @Schema(description = "사용자 이름 입니다.", example = "rtan")
    @Column(unique = true)
    private String username;

    @Schema(description = "유저 권한", example = "USER")
    @Enumerated(value = EnumType.STRING)
    @Column
    private UserRoleEnum role = UserRoleEnum.USER;


    @Builder
    public User(String email, String phoneNumber, String password,String name,String username) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.username = username;
    }
}
