package com.sparta.hh2stagram.domain.follow.dto;

import lombok.Getter;

@Getter
public class FollowResponseDto {

    @Getter
    public static class FollowerResponseDto {
        private final Long followerId;
        private final String followerUsername;

        public FollowerResponseDto(Long followerId, String followerUsername) {
            this.followerId = followerId;
            this.followerUsername = followerUsername;
        }
    }

    @Getter
    public static class FollowingResponseDto {
        private final Long followingId;
        private final String followingUsername;

        public FollowingResponseDto(Long followingId, String followingUsername) {
            this.followingId = followingId;
            this.followingUsername = followingUsername;
        }
    }
}
