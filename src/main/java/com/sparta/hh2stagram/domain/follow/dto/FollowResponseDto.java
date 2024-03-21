package com.sparta.hh2stagram.domain.follow.dto;

import lombok.Getter;

@Getter
public class FollowResponseDto {
    private final Long id;
    private final Long followerId;
    private final String followerUsername;
    private final Long followingId;
    private final String followingUsername;

    public FollowResponseDto(Long id, Long followerId, String followerUsername, Long followingId, String followingUsername) {
        this.id = id;
        this.followerId = followerId;
        this.followerUsername = followerUsername;
        this.followingId = followingId;
        this.followingUsername = followingUsername;
    }
}
