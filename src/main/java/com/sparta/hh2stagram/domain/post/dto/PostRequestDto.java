package com.sparta.hh2stagram.domain.post.dto;

import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostRequestDto {

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    public static class CreatePostRequestDto {

        @Schema(description = "문구", example = "문구를 입력하세요...")
        private String contents;

        public Post toEntity(User user) {
            return Post.builder()
                    .contents(this.contents)
                    .user(user)
                    .build();
        }
    }

//    @AllArgsConstructor
//    @NoArgsConstructor
//    @Builder
//    @Getter
//    public static class UpdatePostRequestDto {
//
//        @Schema (description = "정보 수정", example = "문구를 입력하세요...")
//        private String contents;
//
//        public Post toEntity(User user) {
//            return Post.builder()
//                    .contents(this.contents)
//                    .user(user)
//                    .build();
//        }
//    }
}
