package com.sparta.hh2stagram.domain.follow.controller;

import com.sparta.hh2stagram.domain.follow.service.FollowService;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@Tag(name = "Follow Controller", description = "팔로우 기능 컨트롤러")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    public FollowController(FollowService followService, UserService userService) {
        this.followService = followService;
        this.userService = userService;
    }

    @Operation(summary = "팔로우", description = "팔로우 추가")
    @ApiResponse(responseCode = "200", description = "팔로우 성공")
    @PostMapping("/user/follow/{userId}")
    public ResponseEntity follow(@PathVariable Long userId, @AuthenticationPrincipal UserDetails userDetails) {
        User fromUser = userService.findById(userDetails.getUsername()); // 현재 사용자 정보 가져오기
        User toUser = userService.findById(userId); // 팔로우할 대상 사용자 정보 가져오기
        followService.follow(fromUser, toUser); // FollowService의 follow 메소드 호출
        return ResponseEntity.ok().build();
    }
}
//        String message =followService.followOrUnfollowUser(userId, userDetails);
//        return ResponseEntity.status(HttpStatus.OK).body(message);
//    }
//}
