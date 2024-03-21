package com.sparta.hh2stagram.domain.follow.repository;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User follower); // Changed to findByFollower
    List<Follow> findByFollowing(User following);

    void deleteFollowByFollower(User follower); // Changed to deleteFollowByFollower
    void deleteFollowByFollowing(User following);

    @Query("SELECT f FROM Follow f WHERE f.follower = :from AND f.following = :to")
    Optional<Follow> findFollow(@Param("from") User fromUser, @Param("to") User toUser);
}
