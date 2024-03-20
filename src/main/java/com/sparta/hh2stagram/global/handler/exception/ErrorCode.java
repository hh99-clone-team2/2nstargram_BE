package com.sparta.hh2stagram.global.handler.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다.", HttpStatus.BAD_REQUEST),
    MEMBER_ACCOUNT_NOT_FOUND("찾을 수 없는 계정입니다.", HttpStatus.BAD_REQUEST),
    COMMENT_ID_NOT_FOUND("찾을 수 없는 댓글 번호입니다.", HttpStatus.BAD_REQUEST),
    GAME_ID_NOT_FOUND("찾을 수 없는 게임 번호입니다.", HttpStatus.BAD_REQUEST),
    CHOICE_ID_NOT_FOUND("찾을 수 없는 선택지 번호입니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCH_MEMBER_ACCOUNT("일치하지 않는 계정입니다.", HttpStatus.BAD_REQUEST),
    IMAGE_EMPTY("사진을 넣어주세요.", HttpStatus.BAD_REQUEST),
    IMSGE("안쪽에서 사진이 걸렸음", HttpStatus.BAD_REQUEST);

    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}