package com.sparta.hh2stagram.domain.follow.service;

import com.sparta.hh2stagram.domain.follow.dto.FollowResponseDto;
import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.follow.repository.FollowRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.service.UserService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j(topic = "팔로우 서비스")
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Transactional
    public String follow(User fromUser, User toUser) {
        // 자기 자신 follow 안됨
        if (fromUser == toUser)
            throw new CustomApiException("자기 자신은 follow 할 수 없습니다.");
        // 중복 follow x
        if (followRepository.findFollow(fromUser, toUser.getId()).isPresent())
            throw new CustomApiException("이미 follow 되어있습니다.");
        Follow follow = Follow.builder()
                .follower(fromUser)
                .following(toUser.getId())
                .build();
        followRepository.save(follow);
        return "Success";
    }

    @Transactional
    public String unfollow(User fromUser, User toUser) {
        // 자기 자신 unfollow 안됨
        if (fromUser.equals(toUser)) {
            throw new CustomApiException("자기 자신은 unfollow 할 수 없습니다.");
        }
        // 언팔로우 시도한 팔로우 관계 찾기
        Optional<Follow> existingFollow = followRepository.findFollow(fromUser, toUser.getId());
        if (existingFollow.isEmpty()) {
            throw new CustomApiException("이미 unfollow 되어있습니다.");
        }
        followRepository.delete(existingFollow.get());
        return "언팔로우 되었습니다.";
    }

    public List<FollowResponseDto.FollowingResponseDto> getFollowingList(String username) {
        User user = userService.findByUsername(username);
        List<Follow> following = followRepository.findByFollower(user);
        return following.stream()
                .map(this::convertToFollowingDto)
                .toList();
    }

    public List<FollowResponseDto.FollowerResponseDto> getFollowerList(String username) {
        User user = userService.findByUsername(username);
        List<Follow> followers = followRepository.findByFollowingUserId(user.getId());
        return followers.stream()
                .map(this::convertToFollowerDto)
                .toList();
    }

    private FollowResponseDto.FollowingResponseDto convertToFollowingDto(Follow following) {
        return new FollowResponseDto.FollowingResponseDto(
                following.getFollowingUserId(),
                userService.findById(following.getFollowingUserId()).getUsername()
        );
    }

    private FollowResponseDto.FollowerResponseDto convertToFollowerDto(Follow follower) {
        return new FollowResponseDto.FollowerResponseDto(
                follower.getFollower().getId(),
                follower.getFollower().getUsername()
        );
    }
}
