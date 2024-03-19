package com.sparta.hh2stagram.global.refreshToken.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@Table(name = "refreshtoken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @Schema(description = "refresh token")
    private String token;

    @Schema(description = "토큰 발급 사용자 email 정보")
    private String email;

    @Schema(description = "토큰 만료 시간")
    private Date expiryDate;

    @Builder
    public RefreshToken(String token, String email, Date expiryDate) {
        this.token = token;
        this.email = email;
        this.expiryDate = expiryDate;
    }
}