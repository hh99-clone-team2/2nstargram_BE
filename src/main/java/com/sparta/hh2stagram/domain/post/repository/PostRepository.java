package com.sparta.hh2stagram.domain.post.repository;

import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Object> findByIdAndUser(Long postId, User user);
}
