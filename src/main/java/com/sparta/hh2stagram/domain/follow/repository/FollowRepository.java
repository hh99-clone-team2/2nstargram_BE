package com.sparta.hh2stagram.domain.follow.repository;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFollower(User user);

    List<Follow> findByFollowingUserId(Long followingId);

    @Query("SELECT f FROM Follow f WHERE f.follower = :from AND f.followingUserId = :to")
    Optional<Follow> findFollow(@Param("from") User user, @Param("to") Long toUserId);

    @Query("SELECT f FROM Follow f WHERE f.follower.username <= :username AND f.id > :cursorId ORDER BY f.id ASC")
    Slice<Follow> findByFollowingUsernameWithCursor(@Param("username") String username, @Param("cursorId") Long cursorId, Pageable pageable);

    @Query("SELECT f FROM Follow f WHERE f.followingUserId <= :userId AND f.id > :cursorId AND f.follower.id <> :userId ORDER BY f.id ASC")
    Slice<Follow> findByFollowerIdWithCursor(@Param("userId") Long userId, @Param("cursorId") Long cursorId, Pageable pageable);



}

