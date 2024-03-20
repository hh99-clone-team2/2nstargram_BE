package com.sparta.hh2stagram.domain.post.service;

import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.CreatePostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.UpdatePostResponseDto;
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

        Post savedPost = postRepository.save(post);
        log.info("게시물 데이터베이스 저장 완료");

        saveImgToS3(multipartFileList, post, createImageUrlList, createImageNameList);

        return new CreatePostResponseDto(savedPost, createImageUrlList, createImageNameList);
    }

    // 게시물 수정 (글만 수정)
    public UpdatePostResponseDto updatePost(Long postId, UpdatePostRequestDto requestDto,  User user) {

        Post post = (Post) postRepository.findByIdAndUser(postId, user)
                .orElseThrow(() -> new CustomApiException(ErrorCode.NOT_EXIST_POST));

        post.update(requestDto);

        return new UpdatePostResponseDto(post);
    }

    // 게시물 삭제 //게시물을 삭제할까요? 이 게시물을 삭제하시겠어요?
    public void deletePost(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CustomApiException(ErrorCode.NOT_EXIST_POST));

        postRepository.delete(post);

    }



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
