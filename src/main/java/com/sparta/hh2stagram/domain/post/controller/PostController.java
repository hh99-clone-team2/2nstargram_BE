package com.sparta.hh2stagram.domain.post.controller;

import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto;
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
@RequestMapping("/api/p")
public class PostController {

    private final PostService postService;

    // 게시글 등록
    @Operation(summary = "새 게시물 만들기",
            description = "새 게시물 만들기 : contents, file")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createPost(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFileList,
                                        @RequestPart(value = "createPostRequestDto") CreatePostRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        if (multipartFileList == null || multipartFileList.isEmpty() || multipartFileList.stream().allMatch(file -> file.isEmpty())) {
            log.error("사진을 넣어주세요.");
            throw new CustomApiException("이미지가 존재하지 않습니다.");
        }

        PostResponseDto.PostsResponseDto responseDto = postService.createPost(requestDto, multipartFileList, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseDto.success("게시물이 공유되었습니다.", responseDto));
    }

    // 게시글 수정
    @Operation(summary = "수정",
            description = "정보 수정 : contents")
    @PatchMapping(value = "/{postId}")
    public ResponseEntity<?> updatePost(@RequestBody UpdatePostRequestDto requestDto,
                                        @PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        PostResponseDto.PostsResponseDto responseDto = postService.updatePost(postId, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(ResponseDto.success("게시물이 수정되었습니다.", responseDto));
    }

    // 게시글 삭제
    @Operation(summary = "삭제",
            description = "유저 정보가 일치할 경우, 게시물 삭제 가능")
    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        postService.deletePost(postId, userDetails.getUser());

        return ResponseEntity.ok().body(ResponseDto.success("게시물이 삭제되었습니다.", null));

    }

    // 게시글 전체 조회
    @Operation(summary = "게시물 전체 조회",
            description = "postId를 통한 게시물 상세 조회")
    @GetMapping(value = "/explore")
    public ResponseEntity<?> getPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                     @RequestParam(value = "cursor") Long cursor) throws IOException {
        List<PostResponseDto.PostsResponseDto> responseDtoList = postService.getPost(userDetails.getUser(), cursor);
        return ResponseEntity.ok().body(ResponseDto.success("게시글 전체 조회", responseDtoList));
    }

    // 유저별 게시글 조회(마이페이지, 유저페이지)
    @Operation(summary = "유저별 게시물 조회")
    @GetMapping("/{username}")
    public ResponseEntity<?> getPostByUsername(@PathVariable String username,
                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                               @RequestParam(value = "cursor") Long cursor) {
        PostResponseDto.UserPageResponseDto responseDtoList = postService.getPostByUsername(username, userDetails.getUser(), cursor);
        return ResponseEntity.ok().body(ResponseDto.success("해당 유저 게시물 조회", responseDtoList));
    }

    // 팔로우한 사용자의 게시글 조회(메인페이지)
    @Operation(summary = "팔로우한 유저의 게시물 조회",
            description = "팔로우한 유저의 게시물을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getFollowPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                           @RequestParam(value = "cursor") Long cursor) throws IOException {
        List<PostResponseDto.PostsResponseDto> responseDtoList = postService.getPostsOfFollowedUser(userDetails, cursor);

        return ResponseEntity.ok().body(ResponseDto.success("팔로우한 유저의 게시물 조회", responseDtoList));
    }
}
