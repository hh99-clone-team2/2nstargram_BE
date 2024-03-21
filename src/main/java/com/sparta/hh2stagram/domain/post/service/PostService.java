package com.sparta.hh2stagram.domain.post.service;

import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.AllPostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.CreatePostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.UpdatePostResponseDto;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.post.entity.PostImage;
import com.sparta.hh2stagram.domain.post.repository.PostImageRepository;
import com.sparta.hh2stagram.domain.post.repository.PostRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.global.aws.service.S3UploadService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final S3UploadService s3UploadService;
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;

    // 게시물 등록
    public CreatePostResponseDto createPost(CreatePostRequestDto requestDto, List<MultipartFile> multipartFileList, User user) throws IOException {

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
    @Transactional
    public UpdatePostResponseDto updatePost(Long postId, UpdatePostRequestDto requestDto,  User user) {

        Post post = (Post) postRepository.findByIdAndUser(postId, user)
                .orElseThrow(() -> new CustomApiException("해당 게시물이 존재하지 않습니다."));

        post.update(requestDto);

        return new UpdatePostResponseDto(post);
    }

    // 게시물 삭제 //게시물을 삭제할까요? 이 게시물을 삭제하시겠어요?
    public void deletePost(Long postId, User user) {

        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new CustomApiException("해당 게시물이 존재하지 않습니다."));

        postRepository.delete(post);

    }

    // 게시글 전체 조회
    public List<PostResponseDto.AllPostResponseDto> getPost() {
        // 모든 게시글을 조회합니다.
        List<Post> allPosts = postRepository.findAll();

        // 만약 게시글이 존재하지 않는다면 CustomApiException을 던집니다.
        if (allPosts.isEmpty()) {
            throw new CustomApiException("해당 게시물이 존재하지 않습니다.");
        }

        // 전체 게시글 목록을 AllPostResponseDto로 변환하여 반환합니다.
        return allPosts.stream()
                .map(AllPostResponseDto::new)
                .collect(Collectors.toList());
    }

    // AW3 관련 자료
    private void saveImgToS3(List<MultipartFile> multipartFileList, Post post, List<String> updateImageUrlList, List<String> updateImageNameList) throws IOException {

        // 전달된 MultipartFile 목록에서 각 파일을 순회합니다.
        for (MultipartFile multipartFile : multipartFileList) {

            // 이미지 파일 이름을 생성합니다. 중복을 피하기 위해 UUID를 이용합니다.
            String filename = UUID.randomUUID() + multipartFile.getOriginalFilename();

            // S3에 파일을 업로드하고, 업로드된 이미지의 URL을 받아옵니다.
            String imageUrl = s3UploadService.saveFile(multipartFile, filename);

            // 업로드된 이미지 정보를 저장하기 위해 PostImage 객체를 생성합니다.
            PostImage postImage = PostImage.builder()
                    .url(imageUrl) // 이미지의 URL을 설정합니다.
                    .imageName(multipartFile.getOriginalFilename()) // 이미지의 원래 이름을 설정합니다.
                    .s3name(filename) // S3에 저장된 파일의 이름을 설정합니다.
                    .post(post) // 해당 이미지가 속한 게시물을 설정합니다.
                    .build();

            // 생성된 PostImage 객체를 저장소에 저장합니다.
            postImageRepository.save(postImage);

            // 업로드된 이미지의 URL과 원래 파일 이름을 갱신할 리스트에 추가합니다.
            updateImageUrlList.add(imageUrl);
            updateImageNameList.add(multipartFile.getOriginalFilename());
        }
    }

    // 풀리퀘스트를 하기 위한 주석 타임
//    private void hello(){
//        return null;
//    }

}
