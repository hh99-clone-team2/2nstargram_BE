package com.sparta.hh2stagram.domain.likes.controller;

import com.sparta.hh2stagram.domain.likes.service.LikesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Like Controller", description = "좋아요 기능 컨트롤러")
@Slf4j(topic = "좋아요")
@RestController
@RequestMapping("/api/likes")
public class LikesController {

    /* LikeService : 좋아요 기능 서비스
     * 좋아요 기능 API 호출*/
    private final LikesService likesService;
    public LikesController(LikesService likesService) {
        this.likesService = likesService;
    }

    @Operation(summary = "게시글 좋아요", description = "게시글 좋아요를 추가/취소")
    @ApiResponse(responseCode = "200", description = "게시글 좋아요 추가/취소 성공")
    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likeChoice(@PathVariable Long postId, @AuthenticationPrincipal UserDetails userDetails) {

        String message =likesService.likeOrUnlikePost(postId,userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}