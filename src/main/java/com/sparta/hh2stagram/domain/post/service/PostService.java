package com.sparta.hh2stagram.domain.post.service;

import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.CreatePostResponseDto;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.entity.PostImage;
import com.sparta.hh2stagram.domain.post.repository.PostImageRepository;
import com.sparta.hh2stagram.domain.post.repository.PostRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.global.aws.service.S3UploadService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import com.sparta.hh2stagram.global.handler.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final S3UploadService s3UploadService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    // 게시물 등록
    public CreatePostResponseDto createPost(CreatePostRequestDto requestDto, List<MultipartFile> multipartFileList, User user) throws IOException {

        if (multipartFileList == null) {
            log.error("사진을 넣어주세요.");
            throw new CustomApiException(ErrorCode.IMAGE_EMPTY);
        }

        // Front responseDto 작성용
        List<String> createImageUrlList = new ArrayList<>();
        List<String> createImageNameList = new ArrayList<>();

        Post post = requestDto.toEntity(user);
//
        saveImgToS3(multipartFileList, post, createImageUrlList, createImageNameList);

        Post savedPost = postRepository.save(post);
        return new CreatePostResponseDto(savedPost, createImageUrlList, createImageNameList);
//        return new CreatePostResponseDto(savedPost);
    }

    // 게시글 수정

    // 게시글 삭제

    // 게시글 전체 조회

    // AW3 관련 자료
    private void saveImgToS3(List<MultipartFile> multipartFileList, Post post, List<String> updateImageUrlList, List<String> updateImageNameList) throws IOException {
        for (MultipartFile multipartFile : multipartFileList) {
            String filename = UUID.randomUUID() + multipartFile.getOriginalFilename();
            String imageUrl = s3UploadService.saveFile(multipartFile, filename);
            PostImage postImage = PostImage.builder()
                    .url(imageUrl)
                    .imageName(multipartFile.getOriginalFilename())
                    .s3name(filename)
                    .post(post)
                    .build();
            postImageRepository.save(postImage);
            updateImageUrlList.add(imageUrl);
            updateImageNameList.add(multipartFile.getOriginalFilename());
        }
    }
}
