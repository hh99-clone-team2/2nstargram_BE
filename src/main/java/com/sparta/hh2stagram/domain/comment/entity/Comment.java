package com.sparta.hh2stagram.domain.comment.entity;

import com.sparta.hh2stagram.domain.post.entity.Post;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Tag(name= "comment Table", description = "댓글 테이블")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
