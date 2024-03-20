package com.sparta.hh2stagram.domain.follow.service;

import com.sparta.hh2stagram.domain.follow.dto.FollowResponseDto;
import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.follow.repository.FollowRepository;
import com.sparta.hh2stagram.domain.user.entity.User;
import com.sparta.hh2stagram.domain.user.repository.UserRepository;
import com.sparta.hh2stagram.domain.user.service.UserService;
import com.sparta.hh2stagram.global.handler.exception.CustomApiException;
import jakarta.persistence.EntityNotFoundException;
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
        if (followRepository.findFollow(fromUser, toUser).isPresent())
            throw new CustomApiException("이미 follow 되어있습니다.");
        Follow follow = Follow.builder()
                .follower(fromUser)
                .following(toUser)
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
        Optional<Follow> existingFollow = followRepository.findFollow(fromUser, toUser);
        if (existingFollow.isEmpty()) {
            throw new CustomApiException("이미 unfollow 되어있습니다.");
        }
        followRepository.delete(existingFollow.get());
        return "언팔로우 되었습니다.";
    }

    public List<FollowResponseDto> getFollowingList(Long userId) {
        User follower = userService.findById(userId);
        List<Follow> follows = followRepository.findByFollower(follower);
        return follows.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<FollowResponseDto> getFollowerList(Long userId){
        User following = userService.findById(userId);
        List<Follow> follows = followRepository.findByFollowing(following);
        return follows.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private FollowResponseDto convertToDto(Follow follow) {
        return new FollowResponseDto(
                follow.getId(),
                follow.getFollower().getId(),
                follow.getFollower().getUsername(),
                follow.getFollowing().getId(),
                follow.getFollowing().getUsername()
        );

        }
}
