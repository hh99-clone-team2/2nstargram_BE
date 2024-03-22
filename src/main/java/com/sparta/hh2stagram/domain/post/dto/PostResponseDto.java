package com.sparta.hh2stagram.domain.post.dto;

import com.sparta.hh2stagram.domain.comment.dto.CommentResponseDto;
import com.sparta.hh2stagram.domain.comment.entity.Comment;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.entity.PostImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostResponseDto {

    @NoArgsConstructor
    @Getter
    public static class PostsResponseDto {

        private Long postId;
        private String username; // 게시물 작성한 사람
        private String contents;
        private List<PostImageResponseDto> postImageList;
        private int likes;
        private boolean like;
        private List<CommentResponseDto> commentList;
        private LocalDateTime createdAt;

        @Builder
        public PostsResponseDto(Long postId, String username, String contents, List<PostImageResponseDto> postImageList, int likes, boolean like, List<CommentResponseDto> commentList, LocalDateTime createdAt) {
            this.postId = postId;
            this.username = username;
            this.contents = contents;
            this.postImageList = postImageList;
            this.likes = likes;
            this.like = like;
            this.commentList = commentList;
            this.createdAt = createdAt;
        }
    }

    // 유저페이지(마이페이지) 조회
    @NoArgsConstructor
    @Getter
    public static class UserPageResponseDto {

        private List<PostsResponseDto> posts;
        private UserInfoResponseDto userInfo;

        @Builder
        public UserPageResponseDto(List<PostsResponseDto> posts, UserInfoResponseDto userInfo) {
            this.posts = posts;
            this.userInfo = userInfo;
        }
    }

    @NoArgsConstructor
    @Getter
    public static class PostImageResponseDto {
        private Long id;
        private String imageName;
        private String url;

        public PostImageResponseDto(PostImage postImage) {
            this.id = postImage.getId();
            this.url = postImage.getUrl();
            this.imageName = postImage.getImageName();
        }
    }

    @NoArgsConstructor
    @Getter
    public static class UserInfoResponseDto {
        private int postNumber;
        private int follower;
        private int following;

        @Builder
        public UserInfoResponseDto(int postNumber, int follower, int following) {
            this.postNumber = postNumber;
            this.follower = follower;
            this.following = following;
        }
    }
}
