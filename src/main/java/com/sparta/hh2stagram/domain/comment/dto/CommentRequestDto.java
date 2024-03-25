package com.sparta.hh2stagram.domain.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequestDto {
    private String content;

    @Builder
    public CommentRequestDto( String content){
        this.content = content;
    }
}