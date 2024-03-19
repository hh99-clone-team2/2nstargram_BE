package com.sparta.hh2stagram.domain.follow.repository;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
