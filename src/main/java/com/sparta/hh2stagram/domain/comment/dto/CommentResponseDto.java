package com.sparta.hh2stagram.domain.comment.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    private Long postId;
    private Long userId;
    private Long commentId;
    private String username;
    private String content;

    @Builder
    public CommentResponseDto(Long postId, Long userId, Long commentId, String username, String content){
        this.postId = postId;
        this.userId = userId;
        this.commentId = commentId;
        this.username = username;
        this.content = content;
    }
}
