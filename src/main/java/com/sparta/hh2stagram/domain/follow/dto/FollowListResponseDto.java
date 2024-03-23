package com.sparta.hh2stagram.domain.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FollowListResponseDto {
    private final List<FollowResponseDto> follows;
    private final String nextCursor;
}
