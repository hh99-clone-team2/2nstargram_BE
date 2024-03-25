package com.sparta.hh2stagram.domain.likes.repository;

import com.sparta.hh2stagram.domain.likes.entity.Likes;
import com.sparta.hh2stagram.domain.post.entity.Post;
import com.sparta.hh2stagram.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository <Likes, Long> {
    Optional<Likes> findByUserAndPost(User user, Post post);

    Long countById(Long id);
}
