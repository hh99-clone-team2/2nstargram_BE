package com.sparta.hh2stagram.domain.post.entity;

import com.sparta.hh2stagram.domain.comment.entity.Comment;
import com.sparta.hh2stagram.domain.likes.entity.Likes;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.global.refreshToken.entity.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "posts")
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (length = 2200, nullable = true)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImageList;

    @OneToMany(mappedBy = "post")
    private List<Likes> likesList;

    @Builder
    public Post(String contents, User user) {
        this.contents = contents;
        this.user = user;
    }

    public void update(UpdatePostRequestDto requestDto) {
        this.contents = requestDto.getContents();
    }
}
