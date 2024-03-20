package com.sparta.hh2stagram.domain.follow.repository;

import com.sparta.hh2stagram.domain.follow.entity.Follow;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByFromUser(User fromUser);
    List<Follow> findByToUser(User toUser);
    void deleteFollowByFromUser(User fromUser);
    @Query("select f from Follow f where f.fromUser = :from and f.toUser = :to")
    Optional<Follow> findFollow(@Param("from") User fromUser, @Param("to") User toUser);
}
