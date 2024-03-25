package com.sparta.hh2stagram.domain.follow.controller;

import com.sparta.hh2stagram.domain.follow.dto.FollowResponseDto;
import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.follow.service.FollowService;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.service.UserService;
import com.sparta.hh2stagram.global.refreshToken.dto.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/friendships")
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
    @PostMapping("/create/{username}")
    public ResponseEntity<?> follow(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) {
        User fromUser = userService.findByUsername(userDetails.getUsername()); // 현재 사용자 정보 가져오기
        User toUser = userService.findByUsername(username); // 팔로우할 대상 사용자 정보 가져오기
        followService.follow(fromUser, toUser); // FollowService의 follow 메소드 호출
        return ResponseEntity.ok().body(ResponseDto.success("팔로우 되었습니다.", null));
    }

    @Operation(summary = "언팔로우", description = "언팔로우")
    @ApiResponse(responseCode = "200", description = "언팔로우 성공")
    @DeleteMapping("/destroy/{username}")
    public ResponseEntity<?> unfollow(@PathVariable String username, @AuthenticationPrincipal UserDetails userDetails) {
        User fromUser = userService.findByUsername(userDetails.getUsername()); // 현재 사용자 정보 가져오기
        User toUser = userService.findByUsername(username); // 언팔로우할 대상 사용자 정보 가져오기
        followService.unfollow(fromUser, toUser); // FollowService의 unfollow 메소드 호출
        return ResponseEntity.ok().body(ResponseDto.success("언팔로우 되었습니다.", null));
    }

    @Operation(summary = "팔로잉 목록 조회", description = "팔로잉 목록 조회")
    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
    @GetMapping("/{username}/following")
    public ResponseEntity<?> getFollowingList(@PathVariable String username) {
        List<FollowResponseDto.FollowingResponseDto> followingList = followService.getFollowingList(username);
        return ResponseEntity.ok().body(ResponseDto.success("팔로잉 목록 조회", followingList));
    }

    @Operation(summary = "팔로워 목록 조회", description = "팔로워 목록 조회")
    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
    @GetMapping("/{username}/followers")
    public ResponseEntity<?> getFollowerList(@PathVariable String username) {
        User user = userService.findByUsername(username); // 조회할 사용자 정보 가져오기
        List<FollowResponseDto.FollowerResponseDto> followerList = followService.getFollowerList(username); // 팔로워 목록 조회
        return ResponseEntity.ok().body(ResponseDto.success("팔로워 목록 조회", followerList));
    }
}

//    @Operation(summary = "팔로잉 목록 조회", description = "팔로잉 목록 조회")
//    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
//    @GetMapping("/{username}/following")
//    public ResponseEntity<?> getFollowingList(@PathVariable String username, @RequestParam(required = false) Long cursorId) {
//        // 커서 제공 x -> 첫번째 페이지 검색하기 위해 Long.MAX_VALUE로 설정
//        cursorId = cursorId != null ? cursorId : Long.MAX_VALUE;
//        List<FollowResponseDto.FollowingResponseDto> followingList = followService.getFollowingList(username, cursorId);
//        return ResponseEntity.ok().body(ResponseDto.success("팔로잉 목록 조회", followingList));
//    }
//
//    @Operation(summary = "팔로워 목록 조회", description = "팔로워 목록 조회")
//    @ApiResponse(responseCode = "200", description = "팔로워 목록 조회 성공")
//    @GetMapping("/{username}/followers")
//    public ResponseEntity<?> getFollowerList(@PathVariable String username, @RequestParam(required = false) Long cursorId) {
//        cursorId = cursorId != null ? cursorId : Long.MAX_VALUE;
//        List<FollowResponseDto.FollowerResponseDto> followerList = followService.getFollowerList(username, cursorId);
//        return ResponseEntity.ok().body(ResponseDto.success("팔로워 목록 조회", followerList));
//    }
//}