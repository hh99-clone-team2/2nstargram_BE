package com.sparta.hh2stagram.domain.post.controller;

import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.CreatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostRequestDto.UpdatePostRequestDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.CreatePostResponseDto;
import com.sparta.hh2stagram.domain.post.dto.PostResponseDto.UpdatePostResponseDto;
import com.sparta.hh2stagram.domain.post.service.PostService;
import com.sparta.hh2stagram.global.refreshToken.dto.ResponseDto;
import com.sparta.hh2stagram.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> createPost(@RequestPart(value = "files", required = false) List<MultipartFile> multipartFileList,
                                        @RequestPart(value = "createPostRequestDto") CreatePostRequestDto requestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

        CreatePostResponseDto responseDto = postService.createPost(requestDto, multipartFileList, userDetails.getUser());

        return ResponseEntity.ok().body(ResponseDto.success("게시물이 공유되었습니다.", responseDto));
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



}
