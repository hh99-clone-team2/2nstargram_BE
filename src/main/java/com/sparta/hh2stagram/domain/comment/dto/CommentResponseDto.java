package com.sparta.hh2stagram.domain.comment.dto;

import com.sparta.hh2stagram.domain.comment.entity.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponseDto {
    private Long postId;
    private Long userId;
    private Long commentId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private String formattedTime;

    public CommentResponseDto(Comment comment) {
        this.postId = comment.getId();
        this.userId = comment.getUser().getId();
        this.commentId = comment.getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.formattedTime = formatCreatedAt(createdAt);
    }

    @Builder
    public CommentResponseDto(Long postId, Long userId, Long commentId, String username, String content, LocalDateTime createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.commentId = commentId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.formattedTime = formatCreatedAt(createdAt);
    }

    // 생성된 시간을 포맷하는 메서드
    private String formatCreatedAt(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        long hoursPassed = createdAt.until(now, ChronoUnit.HOURS);

        // 시간 간격이 24시간 이하인 경우
        if (hoursPassed < 1) {
            long minutesPassed = createdAt.until(now, ChronoUnit.MINUTES);
            if (minutesPassed <= 1) {
                return "방금 전";
            } else {
                return minutesPassed + "분 전";
            }
        }
        // 시간 간격이 24시간 이상인 경우
        else if (hoursPassed < 24) {
            return hoursPassed + "시간 전";
        }
        // 24시간 이상인 경우
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
            return createdAt.format(formatter);
        }
    }
}