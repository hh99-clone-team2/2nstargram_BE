package com.sparta.hh2stagram.global.handler.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS("이미 가입된 이메일입니다."),
    MEMBER_ACCOUNT_NOT_FOUND("찾을 수 없는 계정입니다."),
    COMMENT_ID_NOT_FOUND("찾을 수 없는 댓글 번호입니다."),
    GAME_ID_NOT_FOUND("찾을 수 없는 게임 번호입니다."),
    CHOICE_ID_NOT_FOUND("찾을 수 없는 선택지 번호입니다."),
    NOT_MATCH_MEMBER_ACCOUNT("일치하지 않는 계정입니다."),
    ;

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}