package com.sparta.hh2stagram.domain.post.dto;

import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.entity.PostImage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PostResponseDto {
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class CreatePostResponseDto {

        private Long id;

        private String contents;

        private List<String> imageName;
        private List<String> imageUrlList;
        private List<PostImageResponseDto> postImageList = new ArrayList<>();

        private LocalDateTime createdAt;
//        private String formattedTime;

        public CreatePostResponseDto(Post post, List<String> urlList, List<String> nameList) {


            this.id = post.getId();
            this.contents = post.getContents();
            this.imageName = nameList;
            this.imageUrlList = urlList;
            this.createdAt = post.getCreatedAt();
//            this.formattedTime = formatCreatedAt(post.getCreatedAt());
        }

        // 생성된 시간을 포맷하는 메서드
//        private String formatCreatedAt(LocalDateTime createdAt) {
//            LocalDateTime now = LocalDateTime.now();
//            long hoursPassed = createdAt.until(now, ChronoUnit.HOURS);
//
//            // 시간 간격이 24시간 이하인 경우 시간을 포맷하여 반환
//            if (hoursPassed <= 24) {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시간");
//                return createdAt.format(formatter);
//            }
//            // 시간 간격이 24시간 이상인 경우 등록된 년/월/일을 반환
//            else {
//                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
//                return createdAt.format(formatter);
//            }
//        }
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
}
