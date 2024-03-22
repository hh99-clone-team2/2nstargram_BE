package com.sparta.hh2stagram.domain.follow.repository;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User user); // Changed to findByFollower
    List<Follow> findByFollowingUserId(Long followingId);

    @Query("SELECT f FROM Follow f WHERE f.follower = :from AND f.followingUserId = :to")
    Optional<Follow> findFollow(@Param("from") User user, @Param("to") Long toUserId);
}
