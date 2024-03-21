package com.sparta.hh2stagram.domain.post.controller;

import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.CreatePostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.UpdatePostResponseDto;
import com.sparta.hh2stagram.domain.post.service.PostService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import com.sparta.hh2stagram.global.refreshToken.dto.ResponseDto;
import com.sparta.hh2stagram.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "게시물 API", description = "게시물 CRUD")
@Slf4j(topic = "PostController 로그")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @Operation(summary = "새 게시물 만들기",
                description = "새 게시물 만들기 : contents, file")
    @PostMapping(value = "/posts", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createPost(@RequestPart(value = "files") List<MultipartFile> multipartFileList,
                                        @RequestPart(value = "createPostRequestDto") CreatePostRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        if (multipartFileList == null || multipartFileList.isEmpty() || multipartFileList.stream().allMatch(file -> file.isEmpty())) {
            log.error("사진을 넣어주세요.");
            throw new CustomApiException("이미지가 존재하지 않습니다.");
        }

        CreatePostResponseDto responseDto = postService.createPost(requestDto, multipartFileList, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success("게시물이 공유되었습니다.", responseDto));
    }

    // 게시글 수정
    @Operation(summary = "수정",
                description = "정보 수정 : contents")
    @PatchMapping(value = "/posts/{postId}")
    public ResponseEntity<?> updatePost(@RequestBody UpdatePostRequestDto requestDto,
                                        @PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        UpdatePostResponseDto requestDto1 = postService.updatePost(postId, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(ResponseDto.success("게시물이 수정되었습니다.", requestDto1));
    }

    // 게시글 삭제
    @Operation(summary = "삭제",
                description = "유저 정보가 일치할 경우, 게시글 삭제 가능")
    @DeleteMapping(value = "/posts/{postId}")
    public ResponseEntity<?> deletePost (@PathVariable Long postId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException{

        postService.deletePost(postId, userDetails.getUser());

        return ResponseEntity.ok().body(ResponseDto.success("게시물이 삭제되었습니다.", null));

    }

    // 게시글 전체 조회
    @Operation(summary = "게시글 전체 조회",
                description = "postId를 통한 게시글 상세 조회")
    @GetMapping(value = "/posts")
    public ResponseEntity<?> getPost () throws IOException {
        List<PostResponseDto.AllPostResponseDto> responseDtoList = postService.getPost();
        return  ResponseEntity.ok().body(ResponseDto.success("게시글 전체 조회", responseDtoList));
    }

//    산하님
//    @Operation(summary = "팔로우한 유저의 게시글 조회",
//            description = "팔로우한 유저의 게시글을 조회합니다.")
//    @GetMapping("/posts/{username}/{followingId}")
//    public ResponseEntity<?> getFollowPost(@PathVariable("username") String username, @PathVariable("followingId") Long followingId,
//                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
//// 인증된 사용자의 권한 확인
//        if (!userDetails.getUsername().equals(username)) {
//            throw new CustomApiException("권한이 없습니다.");
//        }
//
//// 팔로우한 사용자의 게시글 조회
//        List<PostResponseDto> followedUserPosts = postService.getPostsOfFollowedUser(followingId);
//
//        return ResponseEntity.ok().body(followedUserPosts);
//    }

}
