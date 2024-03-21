package com.sparta.hh2stagram.domain.comment.entity;

import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "comment entity", description = "댓글 엔티티")
@Slf4j(topic = "댓글 추가, 조회, 수정, 삭제")
@Entity
@Getter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "댓글 ID")
    private Long id;

    @Column(length = 1000)
    @NotNull(message = "댓글 내용이 비어있습니다.")
    @Schema(description = "댓글 내용", nullable = false, example = "산책재밌어~")
    private String content;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @Schema(description = "회원 ID", nullable = false, example = "1")
    private User user;


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id")
    @Schema(description = "게시물 ID", nullable = false, example = "1")
    private Post post;

    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    public Comment() {}


    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
