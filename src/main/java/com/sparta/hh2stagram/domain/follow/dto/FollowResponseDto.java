package com.sparta.hh2stagram.domain.follow.dto;

import lombok.Getter;

@Getter
public class FollowResponseDto {

    @Getter
    public static class FollowerResponseDto {
        private final Long id;
        private final Long followerId;
        private final String followerUsername;

        public FollowerResponseDto(Long id, Long followerId, String followerUsername) {
            this.id = id;
            this.followerId = followerId;
            this.followerUsername = followerUsername;
        }
    }

    @Getter
    public static class FollowingResponseDto {
        private final Long id;
        private final Long followingId;
        private final String followingUsername;

        public FollowingResponseDto(Long id, Long followingId, String followingUsername) {
            this.id = id;
            this.followingId = followingId;
            this.followingUsername = followingUsername;
        }
    }
}
